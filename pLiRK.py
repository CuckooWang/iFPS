def enrich():
    fall = open (r"EML_all.txt", "r")
    fcir = open (r"EML_down.txt", "r")

    listall = []
    for line in fall.readlines():
        sp = line.strip().split("\t")
        site = sp[0] + "_" + sp[2]
        listall.append(site)

    listcir = []
    for line in fcir.readlines():
        sp = line.strip().split("\t")
        site = sp[0] + "_" + sp[2]
        listcir.append(site)

    N = len(listall)
    M = len(listcir)
    print ("N = " + str(N))
    print ("M = " + str(M))

    ka_all = {}
    ka_sig = {}
    fr = open(r"EML_all.igps.txt", "r")
    #fr = open(r"EML_down.igps.txt","r")
    for line in fr.readlines():
        sp = line.strip().split("\t")
        site = sp[0] + "_" + sp[1]
        ka = sp[6] + "\t" + sp[7]
        if site in listall:
            if ka not in ka_all:
                sites = set()
                sites.add(site)
                ka_all[ka] = sites
            else:
                sites = ka_all[ka]
                sites.add(site)
                ka_all[ka] = sites

        if site in listcir:
            if ka not in ka_sig:
                sites = set()
                sites.add(site)
                ka_sig[ka] = sites
            else:
                sites = ka_sig[ka]
                sites.add(site)
                ka_sig[ka] = sites

    fw = open(r"enrichment_output.txt","w")
    for ka in ka_sig:
        #print (ka)

        n = len(ka_all[ka])
        m = len(ka_sig[ka])
        if m != 0:
           eratio = 1.0 * (m / M) / (n / N)
           pvalue = 100000.0
           if eratio >= 1.0:
               pvalue = hypergeom.sf(m - 1, N, M, n)
           else:
               pvalue = hypergeom.cdf(m, N, M, n)
           # print (str(eratio) + "\t" + str(pvalue))

           fw.write(ka + "\t" + str(eratio) + "\t" +
                    str(pvalue)  + "\t" + str(m) + "\t" + str(M) + "\t" + str(n) + "\t" + str(N) + "\n")

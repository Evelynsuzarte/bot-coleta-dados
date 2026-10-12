//Desenvolvido por: Evelyn Suzarte Fernandes

import org.jsoup.nodes.Document

 class Main {
     static void main(String[] args) {
        Crawler crawler = new Crawler()

        String urlPrincipal = "https://www.gov.br/ans/pt-br/assuntos/prestadores/padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss"
        String pastaDownloads = "src/main/arquivos"

        new File(pastaDownloads + "/Arquivos_padrao_TISS").mkdirs()
        new File(pastaDownloads + "/Historico").mkdirs()
        new File(pastaDownloads + "/Tabelas_relacionadas").mkdirs()

        try {
            Document pagina = crawler.acessarPagina(urlPrincipal)

            String urlTask1 = crawler.buscarLink(pagina, "Clique aqui para acessar a versão Setembro/2026")
            String urlTask2 = crawler.buscarLink(pagina, "Clique aqui para acessar todas as versões dos Componentes")
            String urlTask3 = crawler.buscarLink(pagina, "Clique aqui para acessar as planilhas")

            // Task 1
            if (urlTask1 != null) {
                crawler.baixarComponenteComunicacao(urlTask1, pastaDownloads)
            } else {
                println ("Página da versão não encontrada.")
            }

            // Task 2
            if (urlTask2 != null) {
                crawler.coletarHistorico(urlTask2, pastaDownloads)
            } else {
                println ("Página do histórico não encontrada.")
            }

            // Task 3
            if (urlTask3 != null) {
                crawler.baixarTabelaErros(urlTask3, pastaDownloads)
            } else {
                println ("Página das tabelas não encontrada.")
            }

            println "\nCrawler finalizado!"

        } catch (Exception erro) {
            println ("Erro ao executar o crawler: ${erro.message}")
            erro.printStackTrace()
        }
    }
}
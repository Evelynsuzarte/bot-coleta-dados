//Desenvolvido por: Evelyn Suzarte Fernandes

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

 class Crawler {

    Document acessarPagina (String url) {
        return Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(30000).get()
    }

    String buscarLink(Document pagina, String texto) {
        for (Element link : pagina.select("a[href]")) {
            if (link.text().toLowerCase().contains(texto.toLowerCase())) {
                return link.absUrl("href")
            }
        }
        return null
    }

    // Task 1
    void baixarComponenteComunicacao(String urlVersao, String caminhoDownload) {
        println ("\n1. Os arquivos da documentação do padrão TISS (Troca de Informações na Saúde Suplementar), na versão mais recente")

        Document pagina = acessarPagina(urlVersao)

        for (Element link : pagina.select("a[href]")) {
            String texto = link.text().toLowerCase()
            if (texto.contains("componente de comunicação") && texto.contains(".zip")) {
                String urlArquivo = link.absUrl("href")
                baixarArquivo(urlArquivo, caminhoDownload + "/Arquivos_padrao_TISS", "componente_comunicacao.zip")
                return
            }
        }

        println ("Componente de Comunicação não encontrado.")
    }

    // Task 2
    void coletarHistorico(String urlHistorico, String caminhoDownload) {
        println ("\nTask 2 - Histórico de versões")
        Document pagina = acessarPagina(urlHistorico)
        List<List<String>> historico = []
        for (Element linha : pagina.select("table tr")) {
            List<Element> colunas = linha.select("th, td")
            if (colunas.size() < 3) {
                continue
            }
            String competencia = colunas[0].text().trim()
            String publicacao = colunas[1].text().trim()
            String vigencia = colunas[2].text().trim()

            if (competencia.toLowerCase() == "competência" || competencia.toLowerCase() == "competencia") {
                continue
            }

            if (competenciaValida(competencia)) {
                historico.add([competencia, publicacao, vigencia])
            }
        }

        File arquivo = new File(caminhoDownload + "/Historico/historico_versoes.csv")

        arquivo.withWriter("UTF-8") { escritor ->
            escritor.write("\uFEFF")
            escritor.write("Competência;Publicação;Início de Vigência\n")
            historico.each { dados -> escritor.write(
                    "${dados[0]};${dados[1]};${dados[2]}\n")
            }
        }

        println ("Histórico salvo em: ${arquivo.path}")
    }

    boolean competenciaValida(String competencia) {
        def meses = [
                jan: 1, fev: 2, mar: 3, abr: 4,
                mai: 5, jun: 6, jul: 7, ago: 8,
                set: 9, out: 10, nov: 11, dez: 12
        ]
        def resultado = competencia.toLowerCase() =~ /^(jan|fev|mar|abr|mai|jun|jul|ago|set|out|nov|dez)\/(\d{4})$/

        if (!resultado.matches()) {
            return false
        }

        int mes = meses[resultado[0][1]]
        int ano = resultado[0][2].toInteger()

        return ano > 2016 || (ano == 2016 && mes >= 1)
    }

    // Task 3:
    void baixarTabelaErros(String urlTabelas, String caminhoDownload) {
        println ("\nTask 3 - Tabela de erros")

        Document pagina = acessarPagina(urlTabelas)
        for (Element link : pagina.select("a[href]")) {
            String texto = link.text().toLowerCase()
            if (texto.contains("tabela de erros no envio para a ans")) {
                String urlArquivo = link.absUrl("href")
                baixarArquivo(urlArquivo, caminhoDownload + "/Tabelas_relacionadas", "tabela_erros_envio_ans.xlsx")
                return
            }
        }

        println ("Tabela de erros não encontrada.")
    }


    void baixarArquivo(String urlArquivo, String pasta, String nomeArquivo) {
        File diretorio = new File(pasta)
        diretorio.mkdirs()
        File arquivo = new File(diretorio, nomeArquivo)

        URLConnection conexao = new URL(urlArquivo).openConnection()
        conexao.setRequestProperty("User-Agent", "Mozilla/5.0")
        conexao.setConnectTimeout(30000)
        conexao.setReadTimeout(60000)

        conexao.getInputStream().withCloseable { entrada ->
            arquivo.withOutputStream { saida ->
                saida << entrada
            }
        }
        println ("Arquivo baixado: ${arquivo.path}")
    }
}

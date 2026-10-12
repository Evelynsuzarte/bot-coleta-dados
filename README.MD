# Crawler ANS - Padrão TISS

Projeto desenvolvido em Groovy com a biblioteca Jsoup para automatizar a coleta de informações disponíveis no site da Agência Nacional de Saúde Suplementar (ANS), relacionadas ao padrão TISS (Troca de Informações na Saúde Suplementar).

O crawler acessa as páginas da ANS, identifica os links necessários e realiza o download dos arquivos do componente de comunicação e da tabela de erros. Também coleta o histórico de versões do padrão TISS a partir de janeiro de 2016 e armazena as informações em um arquivo CSV.
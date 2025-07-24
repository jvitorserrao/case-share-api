package com.example.demo.model.service.impl;

import com.example.demo.api.exception.ApiErrorException;
import com.example.demo.model.dto.RequestDTO;
import com.example.demo.model.entity.Autor;
import com.example.demo.model.entity.Categoria;
import com.example.demo.model.entity.Livro;
import com.example.demo.model.repository.AutorRepository;
import com.example.demo.model.service.CategoriaService;
import com.example.demo.model.service.LivroService;
import com.example.demo.model.service.ScrapingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScrapingServiceImpl implements ScrapingService {

    private final WebClient webClient;
    private final AutorRepository autorRepository;
    private final CategoriaService categoriaService;
    private final LivroService livroService;

    @Override
    @Transactional
    public Livro importarLivro(RequestDTO dto) {

        if (Objects.isNull(dto.url()) || dto.url().isBlank()) {
            throw new ApiErrorException("A URL não pode ser nula!");
        }

            log.info("Iniciando importação do livro pela URL: {}", dto.url());

            String html = fetchHtml(dto.url());
            Document document = Jsoup.parse(html);

            String titulo = extrairTitulo(document);
            BigDecimal preco = extrairPreco(document);
            String isbn = extrairIsbn(document);

            livroService.findByIsbn(isbn).ifPresent(l -> {
                throw new ApiErrorException("Livro já cadastrado com ISBN: " + isbn);
            });

            Integer anoPublicacao = extrairAnoPublicacao(document);
            String nomeAutor = extrairNomeAutor(document);

            Autor autor = criarAutor(nomeAutor);
            Categoria categoria = criarCategoriaPadrao();

            validarLivro(titulo, isbn, preco, autor, anoPublicacao);

            Livro livro = criarLivro(titulo, isbn, preco, autor, categoria, anoPublicacao);

            log.info("Livro '{}' importado com sucesso!", titulo);

            return livro;

    }

    private String fetchHtml(String url) {
        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new ApiErrorException("Erro ao buscar ao buscar os dados do livro");
        }
    }


    private String extrairTitulo(Document document) {
        Element titulo = document.selectFirst("#productTitle");
        if (Objects.isNull(titulo) || titulo.text().trim().isBlank()) {
            throw new ApiErrorException("Título do livro não encontrado na página");
        }
        return titulo.text().trim();
    }

    private String extrairNomeAutor(Document document) {
        Element autorElement = document.selectFirst(".author a, .contributorNameID");
        if (Objects.isNull(autorElement) || autorElement.text().isBlank()) {
            throw new ApiErrorException("Autor não encontrado na página");
        }
        return autorElement.text().trim();
    }


    private BigDecimal extrairPreco(Document document) {
        try {
            Element whole = document.selectFirst(".a-price-whole");
            Element fraction = document.selectFirst(".a-price-fraction");

            if (Objects.isNull(whole) || Objects.isNull(fraction)) {
                throw new ApiErrorException("Preço não encontrado");
            }

            return new BigDecimal(
                    whole.text().replaceAll("\\D", "") + "." + fraction.text()
            );
        } catch (NumberFormatException e) {
            throw new ApiErrorException("Preço inválido!");
        }
    }

    private Categoria criarCategoriaPadrao() {
     return categoriaService.save(
                Categoria.builder()
                        .nome("Importado da Amazon")
                        .descricao("Livros importados automaticamente da Amazon")
                        .build()
        );
    }


    private String extrairIsbn(Document document) {
        Elements elementos = document.select(
                "#detailBullets_feature_div li, " +
                        "#productDetails_detailBullets_sections1 tr, " +
                        "#productDetails_techSpec_section_1 tr, " +
                        "#prodDetails tr"
        );

        String isbn10 = null;
        String isbn13 = null;

        for (Element element : elementos) {
            String texto = element.text().toLowerCase();

            if (texto.contains("isbn")) {
                String raw = texto.contains(":")
                        ? texto.substring(texto.indexOf(":") + 1).trim()
                        : texto;

                raw = raw.replaceAll("[^\\dXx]", "");

                if (raw.length() == 13 && raw.matches("\\d{13}")) {
                    isbn13 = raw;
                } else if (raw.length() == 10 && raw.matches("[\\dXx]{10}")) {
                    isbn10 = raw;
                }
            }
        }

        if (isbn13 != null) return isbn13;
        if (isbn10 != null) return isbn10;

        throw new ApiErrorException("ISBN não encontrado ou inválido na página da Amazon.");
    }


    private Autor criarAutor(String nome) {
       return autorRepository.save(
                Autor.builder()
                        .nome(nome)
                        .email(java.text.Normalizer.normalize(nome, java.text.Normalizer.Form.NFD)
                                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                                .replaceAll("[\\s.]", "")
                                .toLowerCase() + "@gmail.com") // email de teste
                        .dataNascimento(LocalDate.of(1970, 1, 1)) // default
                        .build()
        );
    }


    private Integer extrairAnoPublicacao(Document document) {
        Elements elementos = document.select(
                ".informacoes-tecnicas tr, " +
                        "table tr, " +
                        "#detailBullets_feature_div li, " +
                        "#productDetails_detailBullets_sections1 tr"
        );

        for (Element element : elementos) {
            String texto = element.text().toLowerCase();

            if (texto.contains("ano") || texto.contains("publicação") || texto.contains("data")) {
                java.util.regex.Matcher matcher = java.util.regex.Pattern
                        .compile("\\b(19|20)\\d{2}\\b")
                        .matcher(texto);

                if (matcher.find()) {
                    try {
                        return Integer.parseInt(matcher.group());
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        return null;
    }


    private Livro criarLivro(String titulo, String isbn, BigDecimal preco, Autor autor, Categoria categoria, Integer anoPublicacao) {
        Livro livro = Livro.builder()
                .titulo(titulo)
                .isbn(isbn)
                .anoPublicacao(anoPublicacao != null ? anoPublicacao : Year.now().getValue())
                .preco(preco)
                .autor(autor)
                .categoria(categoria)
                .build();

        return livroService.save(livro);
    }

    private void validarLivro(String titulo, String isbn, BigDecimal preco, Autor autor, Integer anoPublicacao) {

        if (Objects.isNull(titulo)) {
            throw new ApiErrorException("Título do livro não pode ser nulo");
        }

        if (Objects.isNull(isbn) || !isbn.matches("\\d{10}|\\d{13}")) {
            throw new ApiErrorException("ISBN deve ter 10 ou 13 dígitos");
        }

        if (Objects.isNull(autor.getEmail()) || !autor.getEmail().matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w+$")) {
            throw new ApiErrorException("Email do autor está em formato inválido");
        }

        if (Objects.isNull(preco) || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new ApiErrorException("Preço deve ser um valor positivo");
        }

        if (Objects.nonNull(anoPublicacao) && anoPublicacao > Year.now().getValue()) {
            throw new ApiErrorException("Ano de publicação não pode ser no futuro");
        }
    }
}
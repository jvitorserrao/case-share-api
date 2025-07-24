package com.example.demo.model.service.impl;

import com.example.demo.api.exception.ApiErrorException;
import com.example.demo.model.dto.LivroDTO;
import com.example.demo.model.entity.Autor;
import com.example.demo.model.entity.Categoria;
import com.example.demo.model.entity.Livro;
import com.example.demo.model.service.AutorService;
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
import java.time.Year;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScrapingServiceImpl implements ScrapingService {

    private final WebClient webClient;
    private final AutorService autorService;
    private final CategoriaService categoriaService;
    private final LivroService livroService;

    @Override
    @Transactional
    public Livro importarLivro(LivroDTO dto) {

        if (Objects.isNull(dto.autorId()) || Objects.isNull(dto.categoriaId()) || Objects.isNull(dto.url())) {
            throw new ApiErrorException("Os parâmetros para realizar a requisição não podem ser nulos!");
        }

        try {
            String html = fetchHtml(dto.url());
            Document document = Jsoup.parse(html);

            String titulo = extrairTitulo(document);
            BigDecimal preco = extrairPreco(document);
            String isbn = extrairIsbn(document);
            Integer anoPublicacao = extrairAnoPublicacao(document);

            Autor autor = findAutorById(dto.autorId());
            Categoria categoria = findCategoriaById(dto.categoriaId());

            validarLivro(titulo, isbn, preco, autor, anoPublicacao);

            return criarLivro(titulo, isbn, preco, autor, categoria, anoPublicacao);
        } catch (Exception e) {
            log.error("Erro ao importar livro", e);
            throw new ApiErrorException("Erro durante o scraping do livro!");
        }
    }

    private String fetchHtml(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String extrairTitulo(Document document) {
        Element titulo = document.selectFirst("#productTitle");
        if (Objects.isNull(titulo) || titulo.text().trim().isBlank()) {
            throw new ApiErrorException("Título do livro não encontrado na página.");
        }
        return titulo.text().trim();
    }


    private BigDecimal extrairPreco(Document document) {
        try {
            Element whole = document.selectFirst(".a-price-whole");
            Element fraction = document.selectFirst(".a-price-fraction");

            if (Objects.isNull(whole) || Objects.isNull(fraction)) {
                throw new ApiErrorException("Elementos de preço não encontrados");
            }

            return new BigDecimal(
                    whole.text().replaceAll("\\D", "") + "." + fraction.text()
            );
        } catch (NumberFormatException e) {
            throw new ApiErrorException("Formato de preço inválido!");
        }
    }


    private String extrairIsbn(Document document) {
        // Tenta nos dois blocos possíveis
        Elements isbnElements = document.select("#detailBullets_feature_div li, #productDetails_detailBullets_sections1 tr");

        for (Element element : isbnElements) {
            String texto = element.text().toLowerCase();
            if (texto.contains("isbn")) {
                String isbn = texto.replaceAll("[^\\dXx]", "");
                if (isbn.length() >= 10 || isbn.length() >= 13) {
                    return isbn;
                }
            }
        }

        throw new ApiErrorException("ISBN não encontrado ou inválido na página da Amazon.");
    }




    private Integer extrairAnoPublicacao(Document document) {
        Elements elements = document.select(".informacoes-tecnicas, table tr");

        for (Element element : elements) {
            if (element.text().toLowerCase().contains("ano")) {
                Element td = element.select("td").last();
                if (Objects.nonNull(td)) {
                    try {
                        return Integer.parseInt(td.text().replaceAll("[^\\d]", ""));
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        return null;
    }

    private Autor findAutorById(Long autorId) {
        return autorService.findById(autorId);
    }

    private Categoria findCategoriaById(Long categoriaId) {
        return categoriaService.findById(categoriaId);
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
        if (Objects.isNull(titulo)){
            throw new ApiErrorException("Título do livro não pode ser vazio.");
        }

        if (Objects.isNull(isbn) || !isbn.matches("\\d{10}|\\d{13}")) {
            throw new ApiErrorException("ISBN deve ter 10 ou 13 dígitos.");
        }

        if (Objects.isNull(autor.getEmail()) || !autor.getEmail().matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w+$")) {
            throw new ApiErrorException("Email do autor está em formato inválido.");
        }

        if (Objects.isNull(preco) || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiErrorException("Preço deve ser um valor positivo.");
        }


        if (Objects.nonNull(anoPublicacao) && anoPublicacao > Year.now().getValue()) {
            throw new ApiErrorException("Ano de publicação não pode ser no futuro.");
        }
    }
}
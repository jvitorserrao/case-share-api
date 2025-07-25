package com.share.case_jr.model.service;

import com.share.case_jr.model.dto.RequestDTO;
import com.share.case_jr.model.entity.Livro;

public interface ScrapingService {
    void importarLivro(RequestDTO dto);
}

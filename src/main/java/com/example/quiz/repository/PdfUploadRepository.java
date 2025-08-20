package com.example.quiz.repository;

import com.example.quiz.entity.PdfUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PdfUploadRepository extends JpaRepository<PdfUpload, Long> {

    Optional<PdfUpload> findByUserId(Long userId);
}

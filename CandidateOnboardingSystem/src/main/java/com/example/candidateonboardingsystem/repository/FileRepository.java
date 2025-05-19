package com.example.candidateonboardingsystem.repository;


import com.example.candidateonboardingsystem.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity,Long> {
}

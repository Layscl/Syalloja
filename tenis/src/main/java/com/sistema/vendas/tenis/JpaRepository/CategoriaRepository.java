package com.sistema.vendas.tenis.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.vendas.tenis.Model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}

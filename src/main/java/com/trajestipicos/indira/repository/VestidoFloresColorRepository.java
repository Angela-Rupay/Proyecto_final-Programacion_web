package com.trajestipicos.indira.repository;

import com.trajestipicos.indira.model.VestidoFloresColor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VestidoFloresColorRepository
        extends JpaRepository<VestidoFloresColor, Long> {

    List<VestidoFloresColor> findByVestido_IdVestido(Long idVestido);
}
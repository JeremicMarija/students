package com.marija.students.service.impl;


import com.marija.students.exception.ResursNijePronadjenException;
import com.marija.students.model.Mesto;
import com.marija.students.repository.MestoRepository;
import com.marija.students.service.MestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class MestoServiceImpl implements MestoService {

    private MestoRepository mestoRepository;

    @Autowired
    public MestoServiceImpl(MestoRepository mestoRepository) {
        this.mestoRepository = mestoRepository;
    }

    @Override
    public Mesto createMesto(Mesto mesto) {
        Optional<Mesto> mestoOptional = mestoRepository.findById(mesto.getPtt());
        if (mestoOptional.isPresent()){
            throw new IllegalStateException("Mesto postoji");
        }
        return mestoRepository.save(mesto);
    }

    @Override
    public List<Mesto> getAllMesta() {
        return mestoRepository.findAll();
    }

    @Override
    public void delete(Long ptt) {
        mestoRepository.deleteById(ptt);
    }

    @Override
    public Optional<Mesto> getMesto(Long ptt) {
        return mestoRepository.findById(ptt);
    }

    @Override
    public Mesto updateMesto(Mesto mesto, Long ptt) {

        Mesto existingMesto = mestoRepository.findById(ptt).orElseThrow(
                () -> new ResursNijePronadjenException("Mesto ", "Ptt= ", ptt));

        existingMesto.setPtt(mesto.getPtt());
        existingMesto.setNaziv(mesto.getNaziv());
        existingMesto.setBrojStanovnika(mesto.getBrojStanovnika());

        mestoRepository.save(existingMesto);

        return  existingMesto;
    }


}

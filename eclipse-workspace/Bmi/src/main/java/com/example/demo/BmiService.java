package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.BmiEntity;
import repository.BmiRepository;


//
@Service
public class BmiService {
	// ①
	@Autowired
	private BmiRepository BmiRepository;

	public List<BmiEntity> findAll() {
		return BmiRepository.findAll();
	}

	public Optional<BmiEntity> findOne(String date) {
		return BmiRepository.findById(date);
	}
	
//適切な課題を与えられてそれをやるって、物凄く楽な環境にいたんだなあ	
//Gitの仕組みがわからない！実は開いたら勝手に認識されてるの？そんなことある？
	

	public BmiEntity save(BmiEntity bmi) {
		return BmiRepository.save(bmi);
	}
	

	public void delete(String date) {
		BmiRepository.deleteById(date);
	}
	
	
	
}
package com.example.demo;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.stereotype.Service;

@Service
public class ValidateService {
	// validateメソッドはオブジェクトを引数として受け取れます。また、オブジェクトを可変で受け取れるようにジェネリクスで定義しています。
	public <T> Set<ConstraintViolation<T>> validate(T target) {

		// Validatorを作成する
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		// 次にバリデーションが行えるようにvalidator変数にバリデーションの関数を格納します。
		Validator validator = factory.getValidator();

		// さいごに、targetを引数にバリデーションを実行し、結果を返却します。
		// バリデーションを実行し、その結果を返す
		return validator.validate(target);
	}
}

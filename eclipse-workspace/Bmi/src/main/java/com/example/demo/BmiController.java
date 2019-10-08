package com.example.demo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import entity.BmiEntity;
import entity.User;
import repository.BmiRepository;
import repository.UserRepository;

@Controller
//@RequestMapping(value = "bmi")
public class BmiController {

	@Autowired
	private BmiService bmiService;

	@Autowired
	private BmiRepository bmiRepository;

	@Autowired
	PasswordEncoder passwdEncoder;

	@Autowired
	UserRepository repository;

	@Autowired
	UserRepository userRepository;

//	コンストラクタだ。
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(@ModelAttribute("formModel") User user, ModelAndView mav) {
		mav.setViewName("add");
		return mav;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
//	@Transactional(readOnly = false)
	public ModelAndView form(@ModelAttribute("formModel") User user, ModelAndView mav) {
		if (userRepository.countByUsername(user.getUsername()) > 0) {
			mav.addObject("obj", user.getUsername() + "は既に使用されてるユーザ名です。");
			mav.setViewName("add");
		} else {
//			入力したパスワードをハッシュ化してくれるよ

			user.setPassword(passwdEncoder.encode(user.getPassword()));
			repository.saveAndFlush(user);
			mav.addObject("user", user.getUsername() + "さん登録完了しました。");
			mav.setViewName("main");
		}
		return mav;
	}

//	初期表示
	@GetMapping(path = "/")
	String login() {
		return "main";
	}

//	入力画面
	@RequestMapping(value = "/login")
//	「@ModelAttribute」アノテーションを付与することで、エンティティクラスのインスタンスを自動的に用意できる
	public String form(@ModelAttribute BmiEntity bmiEntity, Model model) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = dateFormat.format(date);
		bmiEntity.setDate(formattedDate);

		model.addAttribute("date", formattedDate);
		return "index";
	}

//	計算
	@RequestMapping(value = "/calc", method = RequestMethod.POST)
//	@Vilidatedアノテーションを付けると、そのエンティティに対してバリデーションが有効になる
//	バリデーションの結果はBindingResultに格納される
	public String calcBmi(@ModelAttribute("bmiEntity") @Validated BmiEntity bmiEntity, BindingResult result,
			Model model) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = dateFormat.format(date);
		bmiEntity.setDate(formattedDate);

		double height = bmiEntity.getHeight();
		height = height * 0.01;
		int weight = bmiEntity.getWeight();
		double bmiResult = weight / (height * height);

		model.addAttribute("bmiEntity", bmiEntity);

		if (result.hasErrors()) {
			return "index";
		}

		String bmi = String.format("%.1f", bmiResult);
		bmiEntity.setBmi(bmi);
		return "index";
	}

//	保存
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute BmiEntity bmiEntity, Model model) {
		model.addAttribute("bmiEntity", bmiEntity);
		bmiService.save(bmiEntity);
		List<BmiEntity> bmilist = bmiRepository.findAll();
		model.addAttribute("bmilist", bmilist);
		return "list";
	}

	// 履歴画面
	@RequestMapping(value = "/list")
	public String list(@ModelAttribute BmiEntity bmiEntity, Model model) {
		List<BmiEntity> bmilist = bmiRepository.findAll();
		model.addAttribute("bmilist", bmilist);
		return "list";
	}

//	削除
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@ModelAttribute BmiEntity bmiEntity, @RequestParam("date") String date, Model model) {
		bmiService.delete(date);
		List<BmiEntity> bmilist = bmiRepository.findAll();
		model.addAttribute("bmilist", bmilist);
		return "list";
	}

//	編集
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@RequestParam("date") String date, Model model) {
		Optional<BmiEntity> bmiEntity = bmiService.findOne(date);
		BmiEntity bmidata = bmiEntity.get();
		model.addAttribute("bmiEntity", bmidata);
		return "update";
	}

}

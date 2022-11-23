package com.sb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.exception.ResourceNotFoundException;
import com.sb.model.Iteam1;
import com.sb.repository.IteamRepository;
import com.sb.service.IteamService;

@RequestMapping("iteamApi")
@RestController
public class IteamController {

	@Autowired
	IteamService is;

	@Autowired
	IteamRepository ir;

	@PostMapping("/create")
	public ResponseEntity<Iteam1> createIteam(@Valid @RequestBody Iteam1 ii) {
		try {
			Iteam1 prod1 = is.saveOrUpdate(ii);
			return new ResponseEntity<>(prod1, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/iteam/{id}")
	public ResponseEntity<Iteam1> getIteamById(@PathVariable(value = "id") long iteamId)
			throws ResourceNotFoundException {
		Iteam1 pro = ir.findById(iteamId)
				.orElseThrow(() -> new ResourceNotFoundException("Your Entered Iteam Number :: " + iteamId));
		return ResponseEntity.ok().body(pro);
	}

	@GetMapping("/its")

	public ResponseEntity<List<Iteam1>> getAllProducts(@RequestParam(required = false) String name)
			throws ResourceNotFoundException {

		List<Iteam1> items = new ArrayList<Iteam1>();

		if (name == null)
			ir.findAll().forEach(items::add);
		else
			ir.findByNameContaining(name).forEach(items::add);

		if (items.isEmpty()) {
			throw new ResourceNotFoundException("NO Items are found");
		}

		return new ResponseEntity<>(items, HttpStatus.OK);
	}

	@PatchMapping("/iteam/{id}/{cost}")
	public ResponseEntity<Iteam1> updateIteamPartially(@PathVariable Long id, @PathVariable Integer cost) {
		try {
			Iteam1 item = ir.findById(id).get();
			item.setCost(cost);
			return new ResponseEntity<Iteam1>(ir.save(item), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/* 
	@PatchMapping("/iteam/{id}/{cost}")
	public ResponseEntity<Iteam1> updateIteamPartially(@PathVariable Long id, @PathVariable Integer cost) {
		try {
			Iteam1 item = ir.findById(id).get();
			item.setCost(cost);
			return new ResponseEntity<Iteam1>(ir.save(item), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	*/

}

package com.example.demo.controller;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.CriticalPathMethod;
import com.example.demo.model.Task;
import com.example.demo.service.CriticalPathService;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("/api/v1")
public class CritalPathMethodController {



	@Autowired
	private CriticalPathService service;

	
	@ApiResponses(
			value = {
				@ApiResponse(code = 200, message = "Successfully computed critical path method")	
			}
	)
	@GetMapping("/task/criticalpath")
	public CriticalPathMethod getCriticalPath() {
		//TODO read from DB
		try {
			return service.getCriticalPath();
		} catch (Exception e) {
			throw new ResponseStatusException(
			           HttpStatus.NOT_FOUND, "Cannot compute Critical path: ", e);
		}
	}
	

	
	@ApiResponses(
			value = {
				@ApiResponse(code = 200, message = "Successfully save task")	
			}
	)
	@PostMapping("/task")
	public void saveTask(@RequestBody Task task) {
		service.saveOrUpdateTask(task);
	}
	

	@ApiResponses(
			value = {
				@ApiResponse(code = 200, message = "Successfully fetch all task")	
			}
	)
	@GetMapping("/task")
	public HashSet<Task> getAllTask() {
		return service.getAllTask();
	}
	
	
	@ApiResponses(
			value = {
				@ApiResponse(code = 200, message = "Successfully fetch a task")	
			}
	)
	@GetMapping("/task/{id}")
	public Task getTaskById(@PathVariable("id") int id) {
		return service.getTaskById(id);
	}

	
	@ApiResponses(
			value = {
				@ApiResponse(code = 200, message = "Successfully deleted task")	
			}
	)
	@DeleteMapping("/task/{id}")
	public void deleteTask(@PathVariable("id") int id) {
		service.deleteTask(id);
	}

}

package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.CriticalPathMethod;
import com.example.demo.model.Task;
import com.example.demo.repository.ResourceRespository;

@Service
public class CriticalPathService {


	private static final  HashSet<Task> allTasks = new HashSet<Task>();

	static {
		Task end = new Task("End", 0);
	    Task F = new Task("F", 2, end);
	    Task A = new Task("A", 3, end);
	    Task X = new Task("X", 4, F, A);
	    Task Q = new Task("Q", 2, A, X);
	    Task start = new Task("Start", 0, Q);
	    allTasks.add(end);
	    allTasks.add(F);
	    allTasks.add(A);
	    allTasks.add(X);
	    allTasks.add(Q);
	    allTasks.add(start);

	}

	@Autowired
	private ResourceRespository repo;

	private Task[] computeCriticalPath(Set<Task> tasks) {

	
		HashSet<Task> completed = new HashSet<Task>();
		HashSet<Task> remaining = new HashSet<Task>(tasks);

		while(!remaining.isEmpty()){
			boolean progress = false;

			//find a new task to calculate
			for(Iterator<Task> it = remaining.iterator();it.hasNext();){
				Task task = it.next();


				//TODO debug this to why Task object from DB fails containsAll
				if(completed.containsAll(task.getDependencies())){
					int critical = 0;

					for(Task t : task.getDependencies()){
						if(t.getCriticalCost() > critical){
							critical = t.getCriticalCost();
						}
					}
					task.setCriticalCost(critical+task.getCost());
					//set task as calculated an remove
					completed.add(task);
					it.remove();
					progress = true;
				}
			}
			//If we haven't made any progress then a cycle must exist in
			//the graph and we wont be able to calculate the critical path
			if(!progress) throw new RuntimeException("Cyclic dependency, algorithm stopped!");
		}



		//get the tasks
		Task[] ret = completed.toArray(new Task[0]);
		//create a priority list
		Arrays.sort(ret, new Comparator<Task>() {

			@Override
			public int compare(Task o1, Task o2) {
				//sort by cost
				int i= o2.getCriticalCost()-o1.getCriticalCost();
				if(i != 0)return i;

				//using dependency as a tie breaker
				//note if a is dependent on b then
				//critical cost a must be >= critical cost of b
				if(o1.isDependent(o2))return -1;
				if(o2.isDependent(o1))return 1;
				return 0;
			}
		
		});

		return ret;

	}



	public CriticalPathMethod getCriticalPath() throws Exception {
		HashSet<Task> task = new HashSet<Task>();
		// COMMENT OUT LINE BELOW WHEN TESTING FROM DB
		//List<Task> taskList = this.getAllTask();
		//if(taskList.isEmpty()) throw new Exception("Task Empty");
		//task.addAll(this.getAllTask());


		task.addAll(allTasks);

		List<Task> criticalPath =  new LinkedList<>(Arrays.asList(this.computeCriticalPath(task))); 
		criticalPath.remove(0);
		String path = criticalPath.stream()
				.map(Task::getName)
				.collect(Collectors.joining("-"))
				.toString();

		return new CriticalPathMethod(path, maxCost(allTasks));
	}

	public Integer maxCost(Set<Task> tasks) {
		Integer max = -1;
		for (Task t : tasks) {
			if (t.getCriticalCost() > max)
				max = t.getCriticalCost();
		}
		return max;

	}

	public void saveOrUpdateTask(Task task) {
		repo.save(task);
	}

	public Task getTaskById(int id){
		return repo.findById(id).get();
	}

	public HashSet<Task> getAllTask(){
		HashSet<Task> tasks = new HashSet<Task>();
		//List<Task>  tasks = new ArrayList<Task>();
		repo.findAll().forEach(task -> tasks.add(task));
		return tasks;
	}

	public void deleteTask(int id){
		repo.deleteById(id);
	}


}

package com.example.demo.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

//mark class as an Entity 
@Entity
//defining class name as Table name
@Table
public class Task implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false, unique = true)
	private Integer id;
	@Column
	private Integer cost;
	@Column
	private Integer criticalCost;
	@Column
	private String name;
	@Lob
	private HashSet<Task> dependencies = new HashSet<Task>();
	public Task() {}
	public Task(String name, int cost, Task... dependencies) {
		this.name = name;
		this.cost = cost;
		if(dependencies != null) {
			for(Task t : dependencies){
				this.dependencies.add(t);
			}
		}
	
	}


	


	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the cost
	 */
	public Integer getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	/**
	 * @return the criticalCost
	 */
	public Integer getCriticalCost() {
		return criticalCost;
	}
	/**
	 * @param criticalCost the criticalCost to set
	 */
	public void setCriticalCost(Integer criticalCost) {
		this.criticalCost = criticalCost;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the dependencies
	 */
	public HashSet<Task> getDependencies() {
		return dependencies;
	}
	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(HashSet<Task> dependencies) {
		this.dependencies = dependencies;
	}
	@Override
	public String toString() {
		return name+": "+criticalCost;
	}

	
	public boolean isDependent(Task t){
		//is t a direct dependency?
		if(dependencies.contains(t)){
			return true;
		}
		//is t an indirect dependency
		for(Task dep : dependencies){
			if(dep.isDependent(t)){
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		return Objects.hash(cost, criticalCost, dependencies, id, name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		return Objects.equals(cost, other.cost) && Objects.equals(criticalCost, other.criticalCost)
				&& Objects.equals(dependencies, other.dependencies) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name);
	}

	
	





}

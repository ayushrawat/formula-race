/**
 * 
 */
package com.techsquare.carrace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * @author rawat
 *
 */
class FormulaOne {
	
	//Object to handle the Car and its driver
	class Car{
		private int topSpeed;
		private float currentSpeed;
		private int acceleration;
		private float handlingFactor = 0.8f;
		private float currentDistance;
		private int timeTaken;
		
		public Car(int totalCars, int i)
		{
			//initializing top speed to 150+10*i, currentSpeed to 0
			topSpeed = 150 + (10*i);
			currentSpeed = 0;
			//initializing acceleration to 2*i
			acceleration = (2*i);
			//initializing initial heads up
			/*
			 * Since every car is 200m behind its predecessor
			 * let's assume nth car is behind all the cars
			 * and let's call that position as 0m or the starting
			 * point. We'll calculate the initial Heads up for
			 * each car as 200*(n-i)
			 * And the Total distance for ith car would be
			 * 200*(n-i) + Given distance in meters
			 */
			//initializing current distance
			currentDistance = 200*(totalCars - i);
			//initializing time taken to 0
			timeTaken = 0;
		}
		
		private void moveTwoSeconds()
		{
			//s = ut + 1/2at^2
			float distanceTravelledInLast2Sec = ((float)currentSpeed*2) + ((float)(acceleration*4)/2);
			currentDistance += distanceTravelledInLast2Sec;
			
			//v = u + at
			currentSpeed += (acceleration*2);
			
			//time taken
			timeTaken += 2;
		}
		
		private float getCurrentDistanceFromEndPoint(int totalDistance)
		{
			//return 0 in case where the Race ended for Current car
			float currentDist = (float)totalDistance - currentDistance;
			if(currentDist <= 0.0f)
				return 0;
			else
				return currentDist;
		}
		
		private void applyNitro()
		{
			//Whichever is less is made the Current Speed
			currentSpeed = Math.min((currentSpeed*2), topSpeed);
		}
		
		private void reduceSpeedByHF()
		{
			currentSpeed *= handlingFactor;
		}
		
		private float getCurrentSpeed()
		{
			return currentSpeed;
		}
		
		private int getTimeTaken()
		{
			return timeTaken;
		}
	}
	
	class Race{
		private List<Car> cars, finishedCars;
		private int totalDistance;
		
		public Race(int totalDist, int numberOfCars) throws Exception
		{
			if(numberOfCars > 0)
			{
				//initializing the cars
				finishedCars = new ArrayList<Car>();
				cars = new ArrayList<Car>();
				Car car;
				for(int i=1; i<=numberOfCars; i++)
				{
					car = new Car(numberOfCars, i);
					cars.add(car);
					System.out.println("Cars Initialized...");
				}
			}
			else
				throw new Exception("No Cars to Race!");
			
			if (totalDist >= 0)
			{
				//adding the initial heads up to the distance to compensate
				totalDistance = totalDist + 200*(numberOfCars - 1);
			}
			else
				throw new Exception("Race Distance cannot be Negative!");
		}
		
		public List<Car> getFinalCars()
		{
			return this.finishedCars;
		}
		
		public void beginRace() {
			System.out.println("Starting Race...");
			//till the cars are in race
			while(cars.size() > 0)
			{
				float dist;
				Car currentCar;
				//for each car
				Iterator<Car> it = cars.iterator();
				while(it.hasNext())
				{
					currentCar = it.next();
					//check if race finished? yes, take steps
					dist = currentCar.getCurrentDistanceFromEndPoint(totalDistance);
					if(dist == 0)
					{
						finishedCars.add(currentCar);
						it.remove();
						continue;
					}
					//check if any other car is around the current car? yes, take steps
					checkIfGivenCarIsAroundAnyOtherCarAndIfYesReduceSpeed(currentCar, dist);
					//check if the last one? yes, take steps
					checkIfGivenCarIsTheLastOneAndIfYesApplyNitro(currentCar, dist);
				}
				//move each two seconds ahead
				for(Car car:cars)
					car.moveTwoSeconds();
			}
			System.out.println();
		}

		private void checkIfGivenCarIsTheLastOneAndIfYesApplyNitro(Car currentCar, float dist) {
			
			boolean maxTillNow = true;
			
			for(Car car:cars)
			{
				if( dist >= car.getCurrentDistanceFromEndPoint(totalDistance))
					continue;
				maxTillNow = false;
			}
			
			if(maxTillNow)
				currentCar.applyNitro();
		}

		private void checkIfGivenCarIsAroundAnyOtherCarAndIfYesReduceSpeed(Car currentCar, float distance) {
			for(Car car:cars)
			{
				if(Math.abs(distance - car.getCurrentDistanceFromEndPoint(totalDistance)) <= 10 )
				{
					currentCar.reduceSpeedByHF();
					break;
				}
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// take input for number of cars and race distance
		int totalCars = 0;
		int totalDistance = 0;
		String kmOrm = "";
		Scanner input = new Scanner(System.in);
		System.out.println("Enter Total Number of Cars");
		totalCars = Integer.parseInt(input.next());
		System.out.println("Enter Total Distance for the Race");
		totalDistance = Integer.parseInt(input.next());
		System.out.println("Enter Distance unit (Car's end Speed will be returned according to what you enter!): M for Meters, KM for Kilometers");
		kmOrm = input.next();
		if(kmOrm.equalsIgnoreCase("KM"))
			totalDistance *= 1000;
		input.close();
		try {
			FormulaOne f1 = new FormulaOne();
			Race formula1 = f1.new Race(totalDistance, totalCars);
			formula1.beginRace();
			List<Car> finalCars = formula1.getFinalCars();
			for(Car car:finalCars)
			{
				
				if(kmOrm.equalsIgnoreCase("KM"))
				{
					System.out.println("Completion Time for Car " + (finalCars.indexOf(car)+1) + " is: " + ((car.getTimeTaken()/60)/60) + " hours.");
					System.out.println("Completion Speed for Car " + (finalCars.indexOf(car)+1) + " is: " + (car.getCurrentSpeed()*(3.6)) + " Km/H.");
				}
				else
				{
					System.out.println("Completion Time for Car " + (finalCars.indexOf(car)+1) + " is: " + car.getTimeTaken() + " seconds.");
					System.out.println("Completion Speed for Car " + (finalCars.indexOf(car)+1) + " is: " + car.getCurrentSpeed() + " Meter/Sec.");
				}
			}
		} catch (Exception e) {
			System.out.println("xxxxxx!!!xxxx "+e.getMessage()+" xxxxxx!!!xxxxx");
		}
		// give output for each car
	}

}

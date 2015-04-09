/**
 * 
 */
package com.techsquare.carrace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rawat
 *
 */
class FormulaOne {
	
	//Object to handle the Car and its driver
	class Car{
		int topSpeed;
		float currentSpeed;
		int acceleration;
		float handlingFactor = 0.8f;
		int initialHeadsUp;
		float currentDistance;
		int timeTaken;
		
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
			//initializing current distance as the initial Heads up
			currentDistance = initialHeadsUp = 200*(totalCars - i);
			//initializing time taken to 0
			timeTaken = 0;
		}
		
		void moveTwoSeconds()
		{
			//s = ut + 1/2at^2
			float distanceTravelledInLast2Sec = ((float)currentSpeed*2) + ((float)(acceleration*4)/2);
			currentDistance += distanceTravelledInLast2Sec;
			
			//v = u + at
			currentSpeed += (acceleration*2);
			
			//time taken
			timeTaken += 2;
		}
		
		float getCurrentDistanceFromEndPoint(int totalDistance)
		{
			//return 0 in case where the Race ended for Current car
			float currentDist = (float)totalDistance - currentDistance;
			if(currentDist <= 0.0f)
				return 0;
			else
				return currentDist;
		}
		
		void applyNitro()
		{
			//Whichever is less is made the Current Speed
			currentSpeed = Math.min((currentSpeed*2), topSpeed);
		}
		
		void reduceSpeedByHF()
		{
			currentSpeed *= handlingFactor;
		}
		
		float getCurrentSpeed()
		{
			return currentSpeed;
		}
		
		int getTimeTaken()
		{
			return timeTaken;
		}
	}
	
	class Race{
		List<Car> cars, finishedCars;
		int totalDistance;
		
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
				}
			}
			else
				throw new Exception("No Cars to Race!");
			
			if (totalDist >= 0)
			{
				//adding the initial heads up to the distance to compensate
				totalDistance = totalDist + 200*(numberOfCars - 1); 
				//let the race begin!
				beginRace();
			}
			else
				throw new Exception("Race Distance cannot be Negative!");
		}

		void beginRace() {
			
			//till the cars are in race
			while(cars.size() > 0)
			{
				//for each car
				for(Car currentCar: cars)
				{
					//check if race finished? yes, take steps
					//check if any other car is around the current car? yes, take steps
					//check if the last one? yes, take steps
					//move the car ahead
				}
			}
			
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}

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
class Ideone {


	/**
	 * Common function to print Race Logs
	 * @param String message
	 */
	public void printLog(String message)
	{
		System.out.println("// || \\ :: "+ message);
	}

	//Object to handle the Car and its driver
	class Car{
		public int id;
		private int topSpeed;
		private float currentSpeed;
		private int acceleration;
		private float handlingFactor = 0.8f;
		private float currentDistance;
		private int initialHeadsUp;
		private int timeTaken;
		private boolean hasNitroApplied = false;

		/**
		 * Constructor to initialize Cars
		 * @param int totalCars - in the Race
		 * @param int i - the car number
		 */
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
			currentDistance = initialHeadsUp = 200*(totalCars - i);
			//initializing time taken to 0
			timeTaken = 0;
			//initializing id to i
			id = i;
		}


		/**
		 * Gets called every time for each car which is in the race. Moves the car ahead 2 seconds.
		 */
		private void moveTwoSeconds()
		{
			printLog("Car"+id+": CurrentSpeed:"+currentSpeed);
			//s = ut + 1/2at^2
			float distanceTravelledInLast2Sec = ((float)currentSpeed*2) + ((float)(acceleration*4)/2);
			currentDistance += distanceTravelledInLast2Sec;

			//v = u + at
			currentSpeed += (acceleration*2);
			//the car cannot go beyond the top speed
			if(currentSpeed >= topSpeed)
				currentSpeed = topSpeed;

			//time taken
			timeTaken += 2;
		}

		/**
		 * @param int totalDistance of the Race
		 * @return float the Distance left for this car to travel yet
		 */
		private float getCurrentDistanceFromEndPoint(int totalDistance)
		{
			//return 0 in case where the Race ended for Current car
			float currentDist = (float)totalDistance - currentDistance;
			if(currentDist <= 0.0f)
				return 0;
			else
				return currentDist;
		}

		/**
		 * The function is called whenever the Current Car is behind all the race cars.
		 * @param int totalDistance
		 * @param best - whether to choose applying nitro specifically at a suitable point
		 * or just applying it whenever the car is behind all
		 */
		private void applyNitro(int totalDistance, boolean best)
		{
			if(best)
			{
				if((((totalDistance - initialHeadsUp)*3)/4 >= getCurrentDistanceFromEndPoint(totalDistance)
						&&
						(totalDistance - initialHeadsUp)/4 <= getCurrentDistanceFromEndPoint(totalDistance))
						||
						(currentSpeed >= (topSpeed/4)
						&&
						currentSpeed <= ((topSpeed*3)/4)))
				{
					//Whichever is less is made the Current Speed
					if(!hasNitroApplied)
					{
						printLog("Applying Nitro to Car"+id);
						currentSpeed = Math.min((currentSpeed*2), topSpeed);
						hasNitroApplied = true;
					}
				}
			}
			else
			{
				if(currentSpeed > 0)
				{
					//Whichever is less is made the Current Speed
					if(!hasNitroApplied)
					{
						printLog("Applying Nitro to Car"+id);
						currentSpeed = Math.min((currentSpeed*2), topSpeed);
						hasNitroApplied = true;
					}
				}
			}
		}

		/**
		 * As the Name Suggests
		 */
		private void reduceSpeedByHF()
		{
			printLog("Reducing speed for Car"+id);
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

		/**
		 * Initializes the Race
		 * @param int totalDistance
		 * @param int numberOfCars
		 * @throws Exception No Cars to Race, Invalid Race Distance
		 */
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
				printLog("Cars Initialized...");
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

		/**
		 * @return Cars which are finished racing
		 */
		public List<Car> getFinalCars()
		{
			return this.finishedCars;
		}

		/**
		 * Executes the Race and stops when all the Cars have reached to end point.
		 * @param best - whether to choose the nitro application scenario or not
		 */
		public void beginRace(boolean best) {
			printLog("Starting Race...");
			//till the cars are in race
			while(cars.size() > 0)
			{
				//move each two seconds ahead
				for(Car car:cars)
				{
					car.moveTwoSeconds();
				}
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
					checkIfGivenCarIsTheLastOneAndIfYesApplyNitro(currentCar, dist, best);
				}
				System.out.println();
			}
			printLog("Race Ended!");
		}

		/**
		 * Checks If Given Car Is The Last One And If Yes then Applies Nitro
		 * @param Car currentCar
		 * @param float distance
		 */
		private void checkIfGivenCarIsTheLastOneAndIfYesApplyNitro(Car currentCar, float dist, boolean best) {

			boolean maxTillNow = true;

			for(Car car:cars)
			{
				if(car.id == currentCar.id)
					continue;
				if( dist > car.getCurrentDistanceFromEndPoint(totalDistance))
					continue;
				maxTillNow = false;
			}

			if(maxTillNow)
				currentCar.applyNitro(totalDistance, best);
		}

		/**
		 * Checks If Given Car Is Around Any Other Car And If Yes then Reduces Speed
		 * @param Car currentCar
		 * @param float distance
		 */
		private void checkIfGivenCarIsAroundAnyOtherCarAndIfYesReduceSpeed(Car currentCar, float distance) {

			for(Car car:cars)
			{
				if(Math.abs(distance - car.getCurrentDistanceFromEndPoint(totalDistance)) <= 10 && car.id != currentCar.id)
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
		String yesNo = "";
		Scanner input = new Scanner(System.in);
		System.out.println("Enter Total Number of Cars");
		totalCars = Integer.parseInt(input.next());
		System.out.println("Enter Total Distance for the Race");
		totalDistance = Integer.parseInt(input.next());
		System.out.println("Enter Distance unit (Car's end Speed will be returned according to what you enter!): M for Meters, KM for Kilometers");
		kmOrm = input.next();
		if(kmOrm.equalsIgnoreCase("KM"))
			totalDistance *= 1000;
		System.out.println("(Additional) Would you like the cars to use nitro only when their speed/distance is optimal? 1 for yes, 0 for no:");
		yesNo = input.next();
		input.close();
		try {
			Ideone f1 = new Ideone();
			Race formula1 = f1.new Race(totalDistance, totalCars);
			formula1.beginRace(yesNo.equalsIgnoreCase("1"));
			List<Car> finalCars = formula1.getFinalCars();
			System.out.println("!!!!!RESULTS!!!!!");
			// give output for each car
			for(Car car:finalCars)
			{

				if(kmOrm.equalsIgnoreCase("KM"))
				{
					System.out.println("Completion Time for Car " + car.id + " is: " + ((car.getTimeTaken()/60)/60) + " hours.");
					System.out.println("Completion Speed for Car " + car.id + " is: " + (car.getCurrentSpeed()*(3.6)) + " Km/H.");
				}
				else
				{
					System.out.println("Completion Time for Car " + car.id + " is: " + car.getTimeTaken() + " seconds.");
					System.out.println("Completion Speed for Car " + car.id + " is: " + car.getCurrentSpeed() + " Meter/Sec.");
				}
			}
		} catch (Exception e) {
			System.out.println("xxxxxx!!!xxxx "+e.getMessage()+" xxxxxx!!!xxxxx");
		}
	}

}

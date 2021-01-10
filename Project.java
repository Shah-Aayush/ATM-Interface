/*
This code is written by:

1. 19BCE237 - Sakshi Sanghavi
2. 19BCE238 - Harshil Sanghvi
3. 19BCE245 - Aayush Shah

*/

import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.*;

//Class Project which contains al the classes and methods necessary for this program
public class Project
{
	Scanner sc=new Scanner(System.in);

	//Method to check and set valid 4 digit PIN
	short setPin()
	{
		Scanner sc = new Scanner(System.in);
		//Try and catch block for exception handling
		try
		{
			short pin = sc.nextShort();
			int count=0;
			short temp=pin;
			while(temp!=0)
			{
				temp/=10;
				count++;
			}

			if(count!=4)
			{
				System.out.print("\n# Please enter 4-digit PIN. #");
				System.out.print("\nPIN: ");
				return setPin();
			}
			return pin;
		}
		catch(InputMismatchException | StackOverflowError e)
		{
			System.out.print("\n# Please enter 4-digit PIN. #");
			System.out.print("\nPIN: ");
			return setPin();
		}
	}

	//Confirms the PIN entered by user while setting the PIN
	public void confirmPin(short pin)
	{
		try
		{
			System.out.print("\nConfirm PIN: ");
			short pinC = sc.nextShort();

			if (pin != pinC)
			{
				System.out.print("\n# This PIN does not match your initially entered PIN. #");
				confirmPin(pin);
			}
		}
		catch (InputMismatchException | StackOverflowError e)
		{
			System.out.print("\n# This PIN does not match your initially entered PIN. #");
			confirmPin(pin);
		}
	}

	//Utility method to delete an account from database
	//Handling a possible IOException using throws IOException
	private void delete(long acc_num) throws IOException
	{
		File customerDatabase = new File("./Customer/customerDatabase.txt");
		if(!customerDatabase.exists())
		{
			System.out.println("# Customer database doesn't exists! #");
		}

		//Buffered reader for efficient reading of files
		BufferedReader readCustomerDatabase = new BufferedReader(new FileReader("./Customer/customerDatabase.txt"));
		String line = readCustomerDatabase.readLine();	//Reads a line from text

		//Variable flag to check if the account number exists or not
		//It will be 0 if account number is not found and 1 if account number exists
		int flag = 0;
		while(line!=null)
		{
			if(line.equals(Long.toString(acc_num)))
			{
				BufferedReader br = new BufferedReader(new FileReader("./Customer/customerDatabase.txt"));
				//StringBuilder converts a given data to a string and then appends or inserts the characters of that string to the string builder
				StringBuilder sb = new StringBuilder();
				while((line = br.readLine())!=null)
				{
					if(line.equals(Long.toString(acc_num)))
					{
						for(int counter=0;counter<11;counter++)	//Skipping the lines of the account details which is to be deleted
							br.readLine();
					}
					else
					{
						//Appending all the lines to string builder except the account number to be deleted
						sb.append(line);
						sb.append("\n");
						for(int counter=0;counter<11;counter++)
						{
							line = br.readLine();
							sb.append(line);
							sb.append("\n");
						}
					}
				}

				//FileWriter to write the data of StringBuilder into file
				//Boolean is given as false as we ant to write the file from beginning
				FileWriter fw = new FileWriter("./Customer/customerDatabase.txt",false);
				fw.write(sb.toString());	//Writing whole data in file
				fw.close();
				br.close();
				System.out.println("\nAccount with account no. "+acc_num + " closed successfully!");
				flag = 1;
				break;
			}
			else
			{
				//Skipping lines of the details for which account number does not match
				for (int counter=0;counter<10;counter++)
					readCustomerDatabase.readLine();
			}
			readCustomerDatabase.readLine();
			line = readCustomerDatabase.readLine();

		}
		if(flag==0)
			System.out.println("\n# No account associated with the entered account number exists! #");

		readCustomerDatabase.close();
	}

	//Method to delete an account
	private void deleteCustomer(long acc_num) throws IOException
	{
		//Variable 'check' that whether the account exists in database or not
		int check = 0;
		File f = new File("./Customer/customerDatabase.txt");
		if(!f.exists())
		{
			System.out.println("# Customer database doesn't exists! #");
			return;
		}

		BufferedReader readCustomerDatabase = new BufferedReader(new FileReader("./Customer/customerDatabase.txt"));
		String line = readCustomerDatabase.readLine();
		while(line!=null)
		{
			if(line.equals(Long.toString(acc_num)))
			{
				check=1;
				System.out.print("Please enter the 4 digit PIN of your account number : ");
				short pin = sc.nextShort();
				line = readCustomerDatabase.readLine();

				//If acc number and PIN matches then deleting that account using delete method which is defined above
				if(line.equals(Short.toString(pin)))
				{
					delete(acc_num);
				}
				else
				{
					System.out.println("\n# Please enter valid credentials! #");
				}
				break;
			}
			else
			{
				for(int i=0;i<10;i++)
				{
					readCustomerDatabase.readLine();
				}
			}
			readCustomerDatabase.readLine();
			line = readCustomerDatabase.readLine();
		}

		if(check==0)
			System.out.println("\n# No account associated with the entered account number exists! #");

		readCustomerDatabase.close();
	}


	//Class Banker which contains all the methods for functions available to admin
	protected class Banker
	{
		Scanner sc = new Scanner(System.in);
		String admin_id = "admin";
		String admin_pass = "admin@123";

		int numOfDays;
		int[] mon = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		int[] leap = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

		boolean checkLeap(int yy)
		{
			return ((yy % 4 == 0) && (yy % 100 != 0)) || (yy % 400 == 0);
		}

		//Method to check whether the entered date is valid or not
		boolean checkDate(int dd, int mm , int yy)
		{
			if ((yy > 2020 || yy < 0) || (mm > 12 || mm < 1))
			{
				return false;
			}
			else
			{
				if (checkLeap(yy))
				{
					numOfDays = leap[mm - 1];
				} else
				{
					numOfDays = mon[mm - 1];
				}

				return dd <= numOfDays && dd >= 1;
			}
		}

		//Method to set valid date
		String setDate()
		{
			Scanner sc = new Scanner(System.in);
			String date = sc.nextLine();
			String[] accDate = date.split("[/\\-]");
			if(!(checkDate(Integer.parseInt(accDate[0]),Integer.parseInt(accDate[1]),Integer.parseInt(accDate[2]))))
			{
				System.out.print("\n# Please enter a valid date. #");
				System.out.print("\nDate(dd/mm/yyyy): ");
				setDate();
			}
			return date;
		}

		//Method to set a valid 10 digit number
		long setNumber()
		{
			Scanner sc = new Scanner(System.in);
			long mobNumber = sc.nextLong();
			long mobNumber1 = mobNumber;

			int count=0;	//Variable for storing length of the number
			while(mobNumber1!=0)
			{
				mobNumber1/=10;
				count++;
			}

			if(count!=10)
			{
				//If the number entered is not valid, it asks user to enter valid number
				System.out.print("\n# Please enter valid 10 digit mobile number. #");
				System.out.print("\nMobile number: ");
				setNumber();
			}
			return mobNumber;
		}

		//Method for creating an admin account
		private void createAdmin() throws IOException
		{
			FileWriter fw = new FileWriter("./Admin/adminDatabase.txt", true);
			System.out.print("\nEnter Admin id: ");
			String admin_id = sc.next();
			System.out.print("Enter Admin password: ");
			String admin_pass = sc.next();
			BufferedWriter bf = new BufferedWriter(fw);

			//Writing admin id and password in the file adminDatabase using BufferedWriter
			bf.write(admin_id + "\n" + admin_pass + "\n*\n");
			bf.close();
			System.out.println("\n# Admin account created successfully! #");

		}

		//Method to delete an admin account from database
		private void deleteAdmin() throws IOException
		{
			int flag = 0;

			System.out.print("\nPlease enter admin id whose data is to be deleted: ");
			admin_id = sc.next();

			System.out.print("Please enter admin password: ");
			admin_pass = sc.next();

			if(admin_id.equals("admin") && admin_pass.equals("admin@123"))
			{
				System.out.println("\n# This id and password are default and cannot be deleted! #");
			}
			else
			{
				File adminDatabase = new File("./Admin/adminDatabase.txt");
				if (!adminDatabase.exists()) {
					//If admin database is empty it will write a default admin id and password in the file
					System.out.println("\n# Admin database doesn't exists! #");
					adminDatabase.createNewFile();
					BufferedWriter writeAdminDatabase = new BufferedWriter(new FileWriter("./Admin/adminDatabase.txt"));
					writeAdminDatabase.write("admin\nadmin@123\n*\n");
					writeAdminDatabase.close();
				}

				BufferedReader readAdminDatabase = new BufferedReader(new FileReader("./Admin/adminDatabase.txt"));
				//Reads a line of text
				String line = readAdminDatabase.readLine();

				while (line != null) {
					if (line.equals(admin_id)) {
						line = readAdminDatabase.readLine();
						if (line.equals(admin_pass)) {
							BufferedReader br = new BufferedReader(new FileReader(new File("./Admin/adminDatabase.txt")));
							StringBuilder sb = new StringBuilder();
							while ((line = br.readLine()) != null) {
								if (line.equals(admin_id)) {
									br.readLine();
									br.readLine();
									continue;
								}
								sb.append(line);
								sb.append("\n");
							}

							//If admin id and password entered by user matches the database,
							//then writing all the data except the matched id and password into the file
							FileWriter fw = new FileWriter("./Admin/adminDatabase.txt", false);
							fw.write(sb.toString());
							fw.close();
							br.close();
							System.out.print("\n# Admin account deleted successfully! #\n");
							flag = 1;
						} else {
							System.out.println("\n# Please enter valid credentials! #");
						}
						break;
					} else {
						readAdminDatabase.readLine();
					}
					readAdminDatabase.readLine();
					line = readAdminDatabase.readLine();
				}

				if (flag == 0)
					System.out.println("\n# Please enter valid credentials! #");

				readAdminDatabase.close();
			}
		}

		//Method to check if account number is valid or not
		//i.e. whether the account number already exists in database or not
		long isValid() throws IOException
		{
			long acc_num = sc.nextLong();
			File f = new File("./Customer/customerDatabase.txt");
			if(!f.exists())
			{
				System.out.println("# Customer database doesn't exists! #");
				return isValid();
			}

			BufferedReader readCustomerDatabase = new BufferedReader(new FileReader("./Customer/customerDatabase.txt"));
			String line = readCustomerDatabase.readLine();

			while(line!=null)
			{
				if(line.equals(Long.toString(acc_num)))
				{
					System.out.print("\nThis account number already exists! Please enter another account number: ");
					isValid();
				}
				else
				{
					for(int i=0;i<10;i++)
					{
						readCustomerDatabase.readLine();
					}
				}
				readCustomerDatabase.readLine();
				line = readCustomerDatabase.readLine();
			}
			readCustomerDatabase.close();
			return acc_num;
		}

		//Method to check whether the user has entered proper input for gender
		String check(String c)
		{
			if(c.toUpperCase().equals("MALE") || c.toUpperCase().equals("FEMALE"))
				return c;
			else
			{
				System.out.print("\nPlease enter valid gender (Male/Female): ");
				String s=sc.next();
				return check(s);
			}
		}

		//Method for admin to view all the details of an account
		private void display(long acc_num) throws  IOException
		{
			int flag = 0;
			FileReader fr = new FileReader("./Customer/customerDatabase.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while(line!=null)
			{
				//If account number matches, then printing all the details on console
				if(line.equals(Long.toString(acc_num)))
				{
					System.out.println("Account number : " + line);
					br.readLine();		//skipping pin
					line = br.readLine();		//going to balance
					System.out.println("Current balance : " + line);
					line = br.readLine();		//going to doa
					System.out.println("Date of Account Creation : " + line);
					line = br.readLine();		//going to name
					System.out.println("Name of account holder : " + line);
					line = br.readLine();		//going to acc type
					System.out.println("Type of account : " + line);
					line = br.readLine();		//going to dob
					System.out.println("DOB of account holder : " + line);
					line = br.readLine();		//going to mobile number
					System.out.println("Mobile number : " + line);
					line = br.readLine();		//going to gender
					System.out.println("Gender : " + line);
					line = br.readLine();		//going to nationality
					System.out.println("Nationality : " + line);
					line = br.readLine();		//going to kyc
					System.out.println("KYC : " + line);
					flag = 1;
					break;
				}
				else
				{
					//Skipping lines of accounts for which account number does not match
					for(int counter = 0;counter<12;counter++)
						line = br.readLine();
				}
			}

			fr.close();
			br.close();

			if(flag==0)
				System.out.println("\n# No account associated with the entered account number exists! #");
		}

		//Method insert to insert all the data in customer database
		private void insert(String acc_type, String name, String gender, String nationality, String kyc, String doa, String dob, long acc_num, short pin, long mob_num, long acc_bal) throws  IOException
		{
			BufferedWriter bf = new BufferedWriter(new FileWriter("./Customer/customerDatabase.txt", true));
			bf.write(acc_num + "\n" + pin + "\n" + acc_bal + "\n" + doa + "\n" + name + "\n" + acc_type + "\n" + dob + "\n" + mob_num + "\n" + gender + "\n" + nationality + "\n" + kyc + "\n" + "*\n");
			bf.close();
			System.out.println("\n# Customer account added successfully! #");

		}

		//Method function that displays the sub menu of functions available for admin and asks for choice
		protected void function() throws IOException
		{
			int option;

			//This loop will run until the user chooses to exit/return to main menu
			while (true)
			{
				System.out.println("\nChoose the number corresponding to function:");
				System.out.println("1. Create a bank account");
				System.out.println("2. Display Account summary");
				System.out.println("3. Close bank account");
				System.out.println("4. Create an admin account");
				System.out.println("5. Delete an admin account");
				System.out.println("6. Return to main menu");
				System.out.print("\n\tChoice: ");

				Scanner sc = new Scanner(System.in);
				option = sc.nextInt();

				if (option == 6)
					break;

				//Switch case will call the methods according to the choice made by user
				switch (option)
				{
					case 1:	//If admin chooses to create a new account
						System.out.print("\nPlease enter your account number: ");
						long h = isValid();

						System.out.print("\nPlease enter date of account creation (dd/mm/yyyy): ");
						String f = setDate();

						System.out.print("\nPlease enter your name: ");
						sc.nextLine();
						String b = sc.nextLine();

						System.out.print("\nPlease enter your account type: ");
						String a = sc.nextLine();

						System.out.print("\nPlease enter your date of birth (dd/mm/yyyy): ");
						String g = setDate();

						System.out.print("\nPlease enter your Mobile number: ");
						long j = setNumber();

						System.out.print("\nPlease enter your gender (Male/Female): ");
						String c = sc.nextLine();
						c= check(c);

						System.out.print("\nPlease enter your nationality: ");
						String d = sc.nextLine();

						System.out.print("\nPlease enter your KYC document: ");
						String e = sc.nextLine();

						System.out.print("\nPlease set your 4-digit PIN: ");
						short i = setPin();
						confirmPin(i);

						System.out.print("\nPlease enter your initial balance: ");
						long k = sc.nextLong();

						//Calling insert method to create an account
						insert(a, b, c, d, e, f, g, h, i, j, k);
						break;

					case 2:	//If admin chooses to view the details if an account
						System.out.print("\nPlease enter account number whose data is to be displayed: ");
						long num=sc.nextLong();
						display(num);
						break;

					case 3:	//If admin chooses to delete a particular account
						System.out.print("\nPlease enter account number whose data is to be deleted: ");
						num=sc.nextLong();
						delete(num);
						break;

					case 4:	//If admin chooses to create an admin account
						createAdmin();
						break;

					case 5:	//If admin chooses to delete an admin account
						deleteAdmin();
						break;

					default:	//It will print appropriate message if invalid choice is entered
						System.out.println("\n# Invalid Choice. #");
						break;
				}
			}
		}
	}

	//Class ATM which contains all methods for the functions available to customer
	protected class ATM
	{
		long acc_num;

		//Method to withdraw particular amount from an account
		private void withdraw(long acc_num) throws IOException
		{
			//Variable check to check existence of account number
			//Variable flag will be 1 if amount is withdrawn
			int amount,flag,check=0;
			long currentBalance = 0;
			flag = 0;

			File f = new File("./Customer/customerDatabase.txt");

			if(!f.exists())
			{
				System.out.println("\n# Customer database doesn't exists! #");
				return;
			}

			BufferedReader readCustomerDatabase = new BufferedReader(new FileReader("./Customer/customerDatabase.txt"));
			String line = readCustomerDatabase.readLine();

			while(line!=null)
			{
				if(line.equals(Long.toString(acc_num)))
				{
					check=1;
					//If account number entered by user matches, it will ask to enter PIN
					System.out.print("Please enter the 4 digit PIN of your account number: ");
					short pin = sc.nextShort();
					line = readCustomerDatabase.readLine();

					//If PIN matches, user will be asked to enter amount which is to be withdrawn
					if(line.equals(Short.toString(pin)))
					{
						line = readCustomerDatabase.readLine();

						System.out.print("\nPlease enter the amount to withdraw: ");
						amount = sc.nextInt();
						currentBalance = Long.parseLong(line);
						//Checking if the account has sufficient balance or not
						if(amount>currentBalance)
						{
							System.out.println("\n# Insufficient balance in your A/C. Cannot process your request. #");
						}
						else
						{
							flag = 1;
							currentBalance-=amount;
							System.out.println("\nSum of Rs. "+amount+" withdrawn successfully. Your updated balance is Rs. "+currentBalance);
						}
					}
					else	//Message will be displayed if PIN does not match
					{
						System.out.println("\n# Please enter valid credentials! #");
					}
					break;

				}
				else
				{
					//Skipping lines of account for which account number does not match
					for(int i=0;i<10;i++)
					{
						readCustomerDatabase.readLine();
					}
				}
				readCustomerDatabase.readLine();
				line=readCustomerDatabase.readLine();
			}
			if(check==0)
			{
				System.out.println("\n# No account associated with the entered account number exists! #");
				return;
			}

			//If the amount is withdrawn, then making necessary changes into file
			if(flag==1)
			{
				BufferedReader br = new BufferedReader(new FileReader(new File("./Customer/customerDatabase.txt")));
				StringBuilder sb = new StringBuilder();

				//Appending all the lines to StringBuilder and changing previous account balance with current account balance
				line = br.readLine();
				sb.append(line);
				sb.append("\n");

				while(line!=null)
				{
					if(line.equals(Long.toString(acc_num)))
					{
						line = br.readLine();
						sb.append(line);
						sb.append("\n");
						br.readLine();
						//Appending current balance instead of previous balance
						sb.append(currentBalance);
						sb.append("\n");
						for(int i=0;i<8;i++)
						{
							line = br.readLine();
							if(line!=null)
							{
								sb.append(line);
								sb.append("\n");
							}
						}
					}
					else
					{
						for(int i=0;i<10;i++)
						{
							line = br.readLine();
							sb.append(line);
							sb.append("\n");
						}
					}

					line = br.readLine();
					if(line!=null)
					{
						sb.append(line);
						sb.append("\n");
					}

					line = br.readLine();
					if(line != null)
					{
						sb.append(line);
						sb.append("\n");
					}
				}
				FileWriter fw = new FileWriter("./Customer/customerDatabase.txt",false);
				fw.write(sb.toString());	//Writing the data from StringBuilder to file
				fw.close();
				br.close();
			}

			readCustomerDatabase.close();
		}

		//Method to deposit particular amount from an account
		private void deposit(long acc_num) throws IOException
		{
			//Variables and objects have same use as explained in withdraw method
			//except that amount will be deposited instead of withdrawn
			int amount,flag=0;
			long currentBalance = 0;
			int check = 0;
			File f = new File("./Customer/customerDatabase.txt");

			if(!f.exists())
			{
				System.out.println("\n# Customer database doesn't exists! #");
				return;
			}

			BufferedReader readCustomerDatabase = new BufferedReader(new FileReader("./Customer/customerDatabase.txt"));
			String line = readCustomerDatabase.readLine();
			while(line!=null)
			{
				if(line.equals(Long.toString(acc_num)))
				{
					check = 1;
					System.out.print("Please enter the 4 digit PIN of your account number: ");
					short pin = sc.nextShort();
					line = readCustomerDatabase.readLine();

					if(line.equals(Short.toString(pin)))
					{
						line = readCustomerDatabase.readLine();

						System.out.print("Please enter the amount to deposit: ");
						amount = sc.nextInt();
						currentBalance = Long.parseLong(line);
						if(amount>25000)
						{
							System.out.println("\n# Sorry, you cannot deposit more than Rs. 25000 at a time. \nPlease try with smaller amount depositions. Thank you. #");
						}
						else
						{
							flag = 1;
							currentBalance+=amount;
							System.out.println("\nSum of Rs. " + amount + " deposited successfully. Your updated balance is Rs. " + currentBalance);
						}

					}
					else
					{
						System.out.println("\n# Please enter valid credentials! #");
					}
					break;
				}
				else
				{
					for(int i=0;i<10;i++)
					{
						readCustomerDatabase.readLine();
					}
				}
				readCustomerDatabase.readLine();
				line = readCustomerDatabase.readLine();
			}

			if(check==0)
				System.out.println("\n# No account associated with the entered account number exists! #");

			if(flag==1)
			{
				BufferedReader br = new BufferedReader(new FileReader(new File("./Customer/customerDatabase.txt")));
				StringBuilder sb = new StringBuilder();
				line = br.readLine();
				sb.append(line);
				sb.append("\n");

				while(line!=null)
				{
					if(line.equals(Long.toString(acc_num)))
					{
						line = br.readLine();
						sb.append(line);
						sb.append("\n");
						br.readLine();
						sb.append(currentBalance);
						sb.append("\n");
						for(int i=0;i<8;i++)
						{
							line = br.readLine();
							if(line!=null)
							{
								sb.append(line);
								sb.append("\n");
							}
						}
					}
					else
					{
						for(int i=0;i<10;i++)
						{
							line = br.readLine();
							sb.append(line);
							sb.append("\n");
						}
					}

					line = br.readLine();
					if(line!=null)
					{
						sb.append(line);
						sb.append("\n");
					}

					line = br.readLine();
					if(line != null)
					{
						sb.append(line);
						sb.append("\n");
					}
				}
				FileWriter fw = new FileWriter("./Customer/customerDatabase.txt",false);
				fw.write(sb.toString());
				fw.close();
				br.close();
			}

			readCustomerDatabase.close();
		}

		//Method to change PIN of an account
		private void changePIN(long acc_num) throws IOException
		{
			int check = 0, flag=0;
			short val = 0;
			File f = new File("./Customer/customerDatabase.txt");

			if(!f.exists())
			{
				System.out.println("\n# Customer database doesn't exists! #");
				return;
			}

			BufferedReader readCustomerDatabase = new BufferedReader(new FileReader("./Customer/customerDatabase.txt"));
			String line = readCustomerDatabase.readLine();
			while(line!=null)
			{
				if(line.equals(Long.toString(acc_num)))
				{
					check = 1;
					System.out.print("Please enter the 4 digit PIN of your account number: ");
					short pin = sc.nextShort();

					line = readCustomerDatabase.readLine();

					if(line.equals(Short.toString(pin)))
					{
						//Asking for the input of new PIN and storing it in val and validating and confirming the new PIN
						System.out.print("\nPlease set your new 4-digit PIN: ");
						val = setPin();
						confirmPin(val);
						flag=1;
					}
					else
					{
						System.out.println("\n# Please enter valid credentials! #");
					}
					break;
				}
				else
				{
					for(int i=0;i<10;i++)
					{
						readCustomerDatabase.readLine();
					}
				}
				readCustomerDatabase.readLine();
				line = readCustomerDatabase.readLine();
			}

			if(check==0)
				System.out.println("\n# No account associated with the entered account number exists! #");

			if(flag==1)
			{
				BufferedReader br = new BufferedReader(new FileReader(new File("./Customer/customerDatabase.txt")));
				StringBuilder sb = new StringBuilder();

				//Appending all the lines to StringBuilder but in place of PIN we will append the new PIN set by user.
				line = br.readLine();
				sb.append(line);
				sb.append("\n");

				while(line!=null)
				{
					if(line.equals(Long.toString(acc_num)))
					{
						br.readLine();
						sb.append(val);
						sb.append("\n");
						for(int i=0;i<9;i++)
						{
							line = br.readLine();
							if(line!=null)
							{
								sb.append(line);
								sb.append("\n");
							}
						}
					}
					else
					{
						for(int i=0;i<10;i++)
						{
							line = br.readLine();
							sb.append(line);
							sb.append("\n");
						}
					}
					for (int i = 0; i < 2; i++) {
						line = br.readLine();
						if(line!=null)
						{
							sb.append(line);
							sb.append("\n");
						}
					}
				}
				FileWriter fw = new FileWriter("./Customer/customerDatabase.txt",false);
				fw.write(sb.toString());	//Writing the data in StringBuilder to file
				System.out.println("\n# Your PIN was changed successfully! #\n");
				fw.close();
				br.close();
			}
			readCustomerDatabase.close();
		}

		//Method to show current balance of an account
		private void showBalance(long acc_num) throws IOException
		{
			int check = 0;
			File f = new File("./Customer/customerDatabase.txt");

			if(!f.exists())
			{
				System.out.println("\n# Customer database doesn't exists! #");
				return;
			}

			BufferedReader readCustomerDatabase = new BufferedReader(new FileReader("./Customer/customerDatabase.txt"));
			String line = readCustomerDatabase.readLine();	//Reads lines of text from the file
			while(line!=null)
			{
				if(line.equals(Long.toString(acc_num)))
				{
					check = 1;
					System.out.print("Please enter the 4 digit PIN of your account number: ");
					short pin = sc.nextShort();

					line = readCustomerDatabase.readLine();

					//If account number an PIN matches with the data in database, then current balance will be displayed on screen
					if(line.equals(Short.toString(pin)))
					{
						System.out.println("\nYour current account balance is Rs. "+readCustomerDatabase.readLine());
					}
					else
					{
						System.out.println("\n# Please enter valid credentials! #");
					}
					break;
				}
				else
				{
					for(int i=0;i<10;i++)
					{
						readCustomerDatabase.readLine();
					}
				}
				readCustomerDatabase.readLine();
				line = readCustomerDatabase.readLine();
			}

			if(check==0)
				System.out.println("\n# No account associated with the entered account number exists! #");

			readCustomerDatabase.close();
		}

		//Method function to display the menu of options available for customer
		protected void function() throws IOException
		{
			int option;
			while (true)
			{
				//Asking customer to select an option
				System.out.println("\nChoose the number corresponding to function:");
				System.out.println("1. Deposit money");
				System.out.println("2. Withdraw money");
				System.out.println("3. Change PIN");
				System.out.println("4. Show account balance");
				System.out.println("5. Close bank account");
				System.out.println("6. Return to main menu");
				System.out.print("\n\tChoice: ");
				Scanner sc = new Scanner(System.in);
				option = sc.nextInt();

				if (option == 6)
					break;

				System.out.print("\nPlease enter your account number: ");
				acc_num = sc.nextLong();

				//Enhanced switch case will execute/call the methods according to the choice made by customer
				switch (option)
				{
					case 1 -> deposit(acc_num);
					case 2 -> withdraw(acc_num);
					case 3 -> changePIN(acc_num);
					case 4 -> showBalance(acc_num);
					case 5 -> deleteCustomer(acc_num);
				}
			}
		}
	}
}

//Class Main which has main method from which the execution starts
class Main
{
	public static void main(String[] args) throws  IOException
	{
		Scanner sc=new Scanner(System.in);

		//Creating objects of ATM and Banker classes
		Project p=new Project();
		Project.ATM a=p.new ATM();
		Project.Banker b=p.new Banker();

		System.out.println("\n*** Welcome to Nirma Bank, Nirma University Branch! ***");

		while(true)
		{
			//Asking user to elect the role(Banker/Customer)
			System.out.print("\n-------------------------");
			System.out.println("\n\tEnter your choice:");
			System.out.println("-------------------------");
			System.out.println("1. Banker");
			System.out.println("2. Customer");
			System.out.println("3. Exit");
			System.out.print("\n\tChoice: ");
			int opt=sc.nextInt();

			if(opt==3)
			{
				System.out.println("\n*** Thank you ***");
				break;
			}

			//According the choice of user calling methods from project class using the objects declared above
			switch (opt)
			{
				case 1:	//If user chooses to operate as a banker
					while(true)
					{
						//Asking for admin id and password
						System.out.print("\nPlease enter admin id: ");
						String id = sc.next();

						System.out.print("Please enter admin password: ");
						String pass = sc.next();

						File adminDatabase = new File("./Admin/adminDatabase.txt");
						if(!adminDatabase.exists())
						{
							System.out.println("# Admin database doesn't exists! #");
							adminDatabase.createNewFile();
							BufferedWriter writeAdminDatabase = new BufferedWriter(new FileWriter("./Admin/adminDatabase.txt"));
							writeAdminDatabase.write("admin\nadmin@123\n*\n");
						}

						BufferedReader readAdminDatabase = new BufferedReader(new FileReader("./Admin/adminDatabase.txt"));
						String line = readAdminDatabase.readLine();
						int flag = 0;

						//This loop will run until the end of file
						while(line!=null)
						{
							if(line.equals(id))
							{
								line = readAdminDatabase.readLine();
								if(line.equals(pass))
								{
									//If id and password matches, function method of Banker class will be called.
									b.function();
									flag = 1;
								}
								else
								{
									System.out.println("\n# Please enter valid credentials! #");
								}
								break;
							}
							else
							{
								readAdminDatabase.readLine();
							}
							readAdminDatabase.readLine();
							line = readAdminDatabase.readLine();
						}
						if(flag == 1)
							break;

						if(line == null)
							System.out.println("\n# Please enter valid credentials! #");

						readAdminDatabase.close();
					}
					break;

				case 2:	//If user chooses to operate as a customer
					a.function();	//Function method of ATM class is called
					break;

				default:	//If user enters invalid choice, then the following message is displayed
					System.out.println("\n# Invalid choice selected! #");
			}
		}
	}
}
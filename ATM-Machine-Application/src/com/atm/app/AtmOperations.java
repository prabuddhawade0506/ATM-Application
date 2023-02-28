package com.atm.app;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class AtmOperations{

	public static void main(String[] args)
			throws NumberFormatException, IOException, SQLException, ClassNotFoundException, ParseException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("==============================================================================");
		System.out.println("=====================  WELCOME TO PRABUDDHA'S ATM ============================");
		System.out.println("==============================================================================");
		System.out.println("\t\t 1 --> CUSTOMER "+"\t\t 2 --> Authorities");
		System.out.println("==============================================================================");
		System.out.println("Enter your choice:");
		int choice = Integer.parseInt(br.readLine());

		if (choice == 1) {

			System.out.println("==============================================================================");
			System.out.println("===========================    LOGIN DETAILS  ================================");
			System.out.println("==============================================================================");

			System.out.print("\t Enter Your Atm Number:");
			String atmNumber = br.readLine();
			System.out.print("\t Enter Your Pin:");
			String atmPin = br.readLine();
	
			try {
				Connection conn = MysqlConnection2.getConnection();
	
				PreparedStatement ps = conn.prepareStatement("select atmPin,accName,block from atmaccounts where atmNumber=?");

				ps.setString(1, atmNumber);
				
	
				ResultSet result1 = ps.executeQuery();
				String password = null;
				String userName=null;
				int blk = 0;
				boolean login = false;
				while (result1.next()) {
					password = result1.getString("atmPin");

						
					 userName = result1.getString("accName");
					blk=result1.getInt("block");
					

					login = true;
				}
					
				
				if (password.equals(atmPin)) {
					System.out
							.println("==============================================================================");
					System.out
							.println("===========================   Login successful ===============================");
					System.out
							.println("==============================================================================");
					System.out.println("============================ WELCOME " + userName.toUpperCase()
							+ " ==========================");
					System.out
							.println("==============================================================================");

					String status = "Y";
					do {
						if(blk==2) {
							System.out.println(" The Atm Cart is Bolcked");
							System.out.println("");
							System.out.println(" kindly Visit Your Brantch");		
							break;
						}

						System.out.println("\t\t  1 --> Deposit Amount"+"\t\t  2 --> Withdraw Amount");
						System.out.println("");
						System.out.println("\t\t  3 --> Balance Check"+"\t\t  4  --> Change Pin");
						System.out.println("");
						System.out.println("\t\t  5 --> Exit/Logout"+"\t\t  ");
						System.out.println();
						System.out.println(
								"==============================================================================");
						System.out.print("Enter your choice:");
						int operationCode = Integer.parseInt(br.readLine());
						ResultSet res;
						switch (operationCode) {
						case 1:
							System.out.println("Enter the deposit amount:");
							double depositAmount = Double.parseDouble(br.readLine());

							ps = conn.prepareStatement("select * from atmaccounts where atmNumber=?");
							ps.setString(1, atmNumber);
							res = ps.executeQuery();
							double existingBalance = 0.0;
							long accId = 0;
							while (res.next()) {
								existingBalance = res.getDouble("accBalance");
								accId = res.getLong("accId");
							}

							existingBalance = existingBalance + depositAmount;

							ps = conn.prepareStatement("update atmaccounts set accBalance=? where atmNumber=?");
							ps.setDouble(1, existingBalance);
							ps.setString(2, atmNumber);

							if (ps.executeUpdate() > 0) {
								ps = conn.prepareStatement("insert into transactions values(?,?,?,?,?,?)");
								Timestamp timestamp = new Timestamp(System.currentTimeMillis());
								long transactionId = timestamp.getTime();


								ps.setLong(1, transactionId);
								ps.setDate(2, new Date(System.currentTimeMillis()));
								ps.setDouble(3, existingBalance);
								ps.setString(4, "deposit");
								ps.setLong(5, accId);
								ps.setLong(6, accId);

								ps.executeUpdate();

								System.out.println(
										"==============================================================================");
								System.out.println("Balance Updated!!");
								System.out.println("New account balance is :" + existingBalance);
								System.out.println(
										"==============================================================================");

							}

							System.out.println("Do you want to continue?(Y/N)");
							status = br.readLine();

							if (status.equals("n") || status.equals("N")) {
								login = false;
							}

							break;
						case 2:
							System.out.println("Enter the withdraw amount:");
							double withdrawAmount = Double.parseDouble(br.readLine());

							ps = conn.prepareStatement("select * from atmaccounts where atmNumber=?");
							ps.setString(1, atmNumber);
							res = ps.executeQuery();
							existingBalance = 0.0;
							accId = 0;
							while (res.next()) {
								existingBalance = res.getDouble("accBalance");
								accId = res.getLong("accId");
							}

							existingBalance = existingBalance - withdrawAmount;

							ps = conn.prepareStatement("update atmaccounts set accBalance=? where atmNumber=?");
							ps.setDouble(1, existingBalance);
							ps.setString(2, atmNumber);

							if (ps.executeUpdate() > 0) {
								ps = conn.prepareStatement("insert into transactions values(?,?,?,?,?,?)");
								Timestamp timestamp = new Timestamp(System.currentTimeMillis());
								long transactionId = timestamp.getTime();

								ps.setLong(1, transactionId);
								ps.setDate(2, new Date(System.currentTimeMillis()));
								ps.setDouble(3, existingBalance);
								ps.setString(4, "withdraw");
								ps.setLong(5, accId);
								ps.setLong(6, accId);

								ps.executeUpdate();

								System.out.println(
										"==============================================================================");
								System.out.println("Balance Updated!!");
								System.out.println("New account balance is :" + existingBalance);
								System.out.println(
										"==============================================================================");

							}

							System.out.println("Do you want to continue?(Y/N)");
							status = br.readLine();

							if (status.equals("n") || status.equals("N")) {
								login = false;
							}

							break;

						case 3:
							ps = conn.prepareStatement("select * from atmaccounts where atmNumber=?");
							ps.setString(1, atmNumber);
							res = ps.executeQuery();
							double balance = 0.0;
							while (res.next()) {
								balance = res.getDouble("accBalance");

							}
							System.out.println(
									"==============================================================================");
							System.out.println("Current account balance is :" + balance);
							System.out.println(
									"==============================================================================");
							System.out.println("Do you want to continue?(Y/N)");
							status = br.readLine();

							if (status.equals("n") || status.equals("N")) {
								login = false;
							}

							break;
						case 4:
							System.out.println("Enter the old pin:");
							String oldPassword = br.readLine();

							System.out.println("Enter the new pin:");
							String newPassword = br.readLine();

							System.out.println("Re-enter the new pin:");
							String rePassword = br.readLine();

							ps = conn.prepareStatement("select * from atmaccounts where atmNumber=?");
							ps.setString(1, atmNumber);

							res = ps.executeQuery();
							String existingPassword = null;
							while (res.next()) {
								existingPassword = res.getString("atmPin");

							}

							if (existingPassword.equals(oldPassword)) {
								if (newPassword.equals(rePassword)) {
									ps = conn.prepareStatement("update atmaccounts set atmPin=? where atmNumber=?");
									ps.setString(1, newPassword);
									ps.setString(2, atmNumber);

									if (ps.executeUpdate() > 0) {
										System.out.println(
												"==============================================================================");
										System.out.println("Pin changed successfully!!");
										System.out.println(
												"==============================================================================");

									} else {
										System.out.println(
												"==============================================================================");
										System.out.println("Problem in password changed!!");
										System.out.println(
												"==============================================================================");

									}

								} else {
									System.out.println(
											"==============================================================================");
									System.out.println("New pin and retype pin must be same!!");
									System.out.println(
											"==============================================================================");

								}
							} else {
								System.out.println(
										"==============================================================================");
								System.out.println("Old pin is wrong!!");
								System.out.println(
										"==============================================================================");

							}
							System.out.println("Do you want to continue?(Y/N)");
							status = br.readLine();

							if (status.equals("n") || status.equals("N")) {
								login = false;
							}
							break;

						case 5:
							login = false;
							break;

						default:
							System.out.println("Wrong Choice!!");
							break;

						}

					} while (login);
					System.out.println("==============================================================================");
					System.out.println("Bye. Have a nice day!!");
					System.out.println("==============================================================================");

				} else {
					System.out
							.println("==============================================================================");
					System.out
							.println("================================  Wrong Pin  ============================");
					System.out
							.println("==============================================================================");
				}
			} catch (Exception e) {
				System.out.println("==============================================================================");
				System.out.println("===========================  Invalid Input ===================================");
				System.out.println("==============================================================================");

			}

		} else if (choice == 2) {

			System.out.println("==============================================================================");
			System.out.println("===========================    LOGIN DETAILS  ================================");
			System.out.println("==============================================================================");

			System.out.print("\t Enter your username:");
			String userName = br.readLine();
			System.out.print("\t Enter your password:");
			String userPassword = br.readLine();
			try {
			Connection conn = MysqlConnection2.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from admin where username=?");
			ps.setString(1, userName);
			ResultSet result = ps.executeQuery();
			String password = null;
			boolean login = false;
			while (result.next()) {
				password = result.getString("password");
				
				login = true;
			}

			if (password.equals(userPassword)) {

				String status = "y";
				System.out.println("=================================================================================");
				System.out.println("=============================   WELCOME ADMIN    ================================");
				System.out.println("=================================================================================");

				do {
					System.out.println("=================================================================================");
					System.out.println("\t\t  1 --> Issue ATM Cart "+"\t\t\t  2 --> Bolck ATM Cart");
					System.out.println("");
					System.out.println("\t\t  3 --> View ATM Transactions"+"\t\t  4 --> Change Password");
					System.out.println("");
					System.out.println("\t\t  5 --> Exit/Logout");
					System.out.println("=================================================================================");

					System.out.println("Enter your choice:");
					int operation = Integer.parseInt(br.readLine());

					switch (operation) {
					case 1:
						System.out.println("Enter customer's full name:");
						String name = br.readLine();

						System.out.println("Enter Atm Number:");
						String uname = br.readLine();

						System.out.println("Gender:");
						String gender = br.readLine();

						System.out.println("Date of Birth:(dd/MM/YYYY)");
						String dob = br.readLine();

						System.out.println("Enter email id:");
						String email = br.readLine();

						System.out.println("Enter phone number:");
						long phone = Long.parseLong(br.readLine());

						System.out.println("Enter address: ");
						String address = br.readLine();

						System.out.println("Enter branch name: ");
						String branch = br.readLine();

						System.out.println("IFSC code:");
						String ifscCode = br.readLine();

						System.out.println("Enter atm pin: ");
						password = br.readLine();

						System.out.println("Re-enter atm pin: ");
						String repassword = br.readLine();

						System.out.println("Set account Id:");
						long accId = Long.parseLong(br.readLine());

						System.out.println("Account type:");
						String accType = br.readLine();

						System.out.println("Initial balance:");
						double balance = Double.parseDouble(br.readLine());
						
						int bk=1;

						ps = conn.prepareStatement("insert into atmaccounts values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
						ps.setLong(1, accId);
						ps.setString(2, name);
						ps.setString(3, uname);
						ps.setString(4, password);
						ps.setDouble(5, balance);
						ps.setString(6, accType);

						SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
						java.util.Date utilDate = format.parse(dob);
						java.sql.Date date = new java.sql.Date(utilDate.getTime());
						ps.setDate(7, date);
						ps.setString(8, gender);
						ps.setString(9, address);
						ps.setString(10, email);
						ps.setLong(11, phone);
						ps.setString(12, branch);
						ps.setString(13, ifscCode);
						ps.setInt(14, bk);

						if (ps.executeUpdate() > 0) {
							System.out.println(
									"==============================================================================");
							System.out.println("ATM Issued successfully!!");
							System.out.println(
									"==============================================================================");

						}

						System.out.println("Do you want to continue?(Y/N)");
						status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login = false;
						}
						break;

					case 2:
						System.out.println("Enter account id:");
						long accountId = Long.parseLong(br.readLine());
						ps = conn.prepareStatement("UPDATE atmaccounts SET block = 2 WHERE accountId = "+accountId+";");
					

						if (ps.executeUpdate() > 0) {
							System.out.println(
									"==============================================================================");
							System.out.println("ATM Block successfully!!");
							System.out.println(
									"==============================================================================");

						} else {
							System.out.println(
									"==============================================================================");
							System.out.println("Problem in ATM Block!!");
							System.out.println(
									"==============================================================================");

						}
						System.out.println("Do you want to continue?(Y/N)");
						status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login = false;
						}
						break;
					case 3:
						System.out.println("Transaction Id \t Date \t Amount \t Type");

						ps = conn.prepareStatement("select * from transactions");
						ResultSet transactions = ps.executeQuery();

						while (transactions.next()) {
							System.out.println(transactions.getLong("transactionId") + "\t"
									+ transactions.getDate("transactionDate")

									+ "\t" + transactions.getDouble("transactionAmount") + "\t"
									+ transactions.getString("transactionType"));
						}
						System.out.println("Do you want to continue?(Y/N)");
						status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login = false;
						}
						break;
					case 4:
						System.out.println("Enter the old password:");
						String oldPassword = br.readLine();

						System.out.println("Enter the new password:");
						String newPassword = br.readLine();

						System.out.println("Re-enter the new password:");
						String rePassword = br.readLine();

						ps = conn.prepareStatement("select * from admin where username=?");
						ps.setString(1, userName);

						result = ps.executeQuery();
						String existingPassword = null;
						while (result.next()) {
							existingPassword = result.getString("password");

						}

						if (existingPassword.equals(oldPassword)) {
							if (newPassword.equals(rePassword)) {
								ps = conn.prepareStatement("update admin set password=? where username=?");
								ps.setString(1, newPassword);
								ps.setString(2, userName);

								if (ps.executeUpdate() > 0) {
									System.out.println(
											"==============================================================================");
									System.out.println("Password changed successfully!!");
									System.out.println(
											"==============================================================================");

								} else {
									System.out.println(
											"==============================================================================");
									System.out.println("Problem in password changed!!");
									System.out.println(
											"==============================================================================");

								}

							} else {
								System.out.println(
										"==============================================================================");
								System.out.println("New password and retype password must be same!!");
								System.out.println(
										"==============================================================================");

							}
						} else {
							System.out.println(
									"==============================================================================");
							System.out.println("Old password is wrong!!");
							System.out.println(
									"==============================================================================");

						}
						System.out.println("Do you want to continue?(Y/N)");
						status = br.readLine();

						if (status.equals("n") || status.equals("N")) {
							login = false;
						}
						break;

					case 5:
						login = false;
						break;

					default:
						System.out.println("Wrong Choice!!");
						break;

					}

				} while (login);
				
				System.out.println("==============================================================================");
				System.out.println("Please Take Your Cart . Have a nice day!!");
				System.out.println("==============================================================================");
				

			} else {
				System.out.println("Enter a valid input!!");
			}
			}catch(Exception e) {

				System.out.println("==============================================================================");
				System.out.println("Invaid Input");
				System.out.println("==============================================================================");
				
				
			}
		}
	}
}

package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.view.ConsoleService;

import java.util.ArrayList;
import java.util.List;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private TransferService transferService;
    private UserService userService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL), new TransferService(API_BASE_URL), new UserService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService, TransferService transferService, UserService userService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;
		this.userService = userService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				System.out.println("Please select a user from this list:\n" +
				"-------------------------------------------\n" +
				"User ID | Username\n" +
				"-------------------------------------------");
				User[] users = userService.listAllUsers();
				List<Integer> userIds = new ArrayList<>();
				for (User user : users) {
					if (user.getId() != null && user.getId() != accountService.getAccount().getUserId()) {
						System.out.println(user.getId() + "    | " + user.getUsername());
						userIds.add(user.getId());
					}
				}

				Integer userTo = null;
				while (userTo == null) {
					try {
						userTo = Integer.parseInt(console.getUserInput("Enter ID of user you are sending to (0 to cancel)"));
					} catch (NumberFormatException e) {
						userTo = null;
						System.out.println("Sorry, that was not valid, please try again.");
						continue;
					}
					if (!userIds.contains(userTo) && userTo != 0) {
						System.out.println("That was not a valid userId.");
						userTo = null;
					}
				}
				if (userTo == 0) {
					continue;
				}
				Double amount = null;
				while (amount == null) {
					try {
						amount = Double.parseDouble(console.getUserInput("Enter the amount you wish to send"));
					} catch (NumberFormatException e) {
						amount = null;
						System.out.println("Sorry, that was not valid, please try again.");
						continue;
					}
					if (amount < 0) {
						System.out.println("You cannot send negative money.");
						amount = null;
					}
				}
				System.out.println("Checking the balance in your account...");
				double amountInAccount = accountService.getAccount().getBalance();
				if (amount > amountInAccount) {
					System.out.println("Sorry, not enough funds. Exiting send menu.");
					continue;
				}
				long userFrom = accountService.getAccount().getAccountId();
				sendBucks(userTo, (int)userFrom, amount);
				System.out.println("Successfully sent your money!");
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance(){
		System.out.println("Your current account balance is: $" + accountService.getAccount().getBalance());
	}

	private void viewTransferHistory() {
		System.out.println("-------------------------------------------\n" +
				"Transfer  |                 |\n" +
				"ID        | From/To         | Amount\n" +
				"-------------------------------------------");
		Transfer[] transfers = transferService.listTransfers();
		for (Transfer transfer : transfers) {
			System.out.println(transfer.getTransferId() + "\t\t To: " + userService.getUsernameByAccountId(transfer.getAccountTo()) + "\t\t $" + transfer.getAmount());
		}
		System.out.println("-------------------------------------------");
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks(int userTo, int userFrom, double amount) {
		Transfer transfer = new Transfer();
		transfer.setAccountTo((int) accountService.getAccountId(userTo));
		transfer.setAccountFrom(userFrom);
		transfer.setAmount(amount);
		transfer.setTransferStatusId(2);
		transfer.setTransferTypeId(2);
		transferService.send(transfer);
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
				String token = (String) currentUser.getToken();
				accountService.AUTH_TOKEN = token;
				transferService.AUTH_TOKEN = token;
				userService.AUTH_TOKEN = token;
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}

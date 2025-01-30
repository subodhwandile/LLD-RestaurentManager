package hotelManagementSystemLLD;

import java.util.*;
import java.util.Map.Entry;

/*1. The restaurant will have different branches.
2. Each restaurant branch will have a menu.
3. The menu will have different menu sections, containing different menu items.
4. The waiter should be able to create an order for a table and add meals for different
seats.
5. Each meal can have multiple meal items corresponding to the menu items.
6. The system should be able to retrieve information about what tables are currently
available to place walk-in customers.
7. The system should support the reservation of tables.
8. The receptionist should be able to search available tables for data/time and reserve a
table.
9. The system should allow customers to cancel their reservation.
10. The system should be able to send notifications whenever the reservation time is
approaching.
11. The customers should be able to pay their bills through credit card, check or cash.
12. Each restaurant branch can have multiple seating arrangements of tables.
*/

public class HotelManagement {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Map<String, List<String>> orderList = new HashMap<String, List<String>>();
		orderList.put("user1", List.of("Paneer", "Roti"));
		RestuarentSystem restuarentSystem = RestuarentSystem.getInstance();
		restuarentSystem.placeOrder("branch1", "branch1Table1", orderList);
	}

}

class RestuarentSystem {
    private static RestuarentSystem instance;
    private Restaurent restuarent;

    private RestuarentSystem() {
    	this.restuarent = new Restaurent("Rest1");

        // Initialize and add a branch to avoid NullPointerException
        Address address = new Address("Street 1", "Sector 10", "Near Park", 123456);
        Menu menu = new Menu();
        Map<String, Table> allTables = new HashMap<>();
        Map<String, Waiter> waiters = new HashMap<>();
        waiters.put("W1", new Waiter("John", "W1"));

        BranchManager branch = new BranchManager("branch1", address, allTables, waiters, menu);
        restuarent.addBranch(branch);
    }

    public static RestuarentSystem getInstance() {
        if (instance == null) { // First check (without locking)
            synchronized (RestuarentSystem.class) { // Locking
                if (instance == null) { // Second check (with locking)
                    instance = new RestuarentSystem();
                }
            }
        }
        return instance;
    }
    public String placeOrder(String branchID, String tableID, Map<String, List<String>> orderList) {

    	String orderID = UUID.randomUUID().toString();
    	Map<String, List<Dish>> orderMap = new HashMap<String, List<Dish>>();
    	for (Entry<String, List<String>> entry : orderList.entrySet()) {
    		
    		List<Dish> dishForUser = new ArrayList<Dish>();
    		for (String dishName : entry.getValue()) {
    			Dish newDish = new Dish(dishName, 100);
    			dishForUser.add(newDish);
    		}
    		orderMap.put(entry.getKey(), dishForUser);
    	}
    	Order newOrder = new Order(orderID, tableID, orderMap);
    	return restuarent.placeOrder(newOrder, branchID);
    }
}


class Restaurent {
	private String name;
	
	private Map<String, BranchManager> branches;

	public Restaurent(String name) {
		this.name = name;
		this.branches = new HashMap<String, BranchManager>();
	}
	
	public void addBranch(BranchManager branch) {
		branches.put(branch.getBranchID(), branch);
	}
	public String placeOrder(Order order, String branchID) {
		BranchManager branchMgr = branches.get(branchID);
		return branchMgr.placeOrder(order);
	}
}

class Address {
	private String firstLine;
	private String secondLine;
	private String landmark;
	private int pincode;
	public Address(String firstLine, String secondLine, String landmark, int pincode) {
		super();
		this.firstLine = firstLine;
		this.secondLine = secondLine;
		this.landmark = landmark;
		this.pincode = pincode;
	}
	public String getFirstLine() {
		return firstLine;
	}
	public void setFirstLine(String firstLine) {
		this.firstLine = firstLine;
	}
	public String getSecondLine() {
		return secondLine;
	}
	public void setSecondLine(String secondLine) {
		this.secondLine = secondLine;
	}
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public int getPincode() {
		return pincode;
	}
	public void setPincode(int pincode) {
		this.pincode = pincode;
	}
}

class Dish {
	private String dishName;
	private double price;
	private MenuType type;
	public Dish(String dishName, double price) {
		super();
		this.dishName = dishName;
		this.price = price;
	}
	public String getDishName() {
		return dishName;
	}
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}
enum MenuType {
	MAIN_COURSE,
	DESERT,
}
class Menu {
	private Map<MenuType, List<Dish>> menuCard;

	public Menu() {
		super();
		this.menuCard = new HashMap<>();
		menuCard.putIfAbsent(MenuType.MAIN_COURSE, new ArrayList<Dish>());
		menuCard.putIfAbsent(MenuType.DESERT, new ArrayList<Dish>());
	}
}
class Seat {
	private String seatID;
	private boolean isOccupied;
	public Seat(String seatID) {
		super();
		this.seatID = seatID;
		this.isOccupied = false;
	}
	public String getSeatID() {
		return seatID;
	}
	public void setSeatID(String seatID) {
		this.seatID = seatID;
	}
	public boolean isOccupied() {
		return isOccupied;
	}
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
}
class Table {
	private String tableID;
	private Map<String, Seat> seats;
	private boolean isReservedAtThisMoment;
}
enum OrderStatus {
	COMPLETED,
	ORDER_PREPARING,
	SERVED
}
class Order {
	private String OrderID;
	private String tableID;
	private Map<String, List<Dish>> orderList;
	private double billForThisOrder;
	private OrderStatus status;
	public Order(String orderID, String table, Map<String, List<Dish>> orderList) {
		super();
		this.OrderID = orderID;
		this.tableID = table;
		this.orderList = orderList;
		this.billForThisOrder = calculateBill();
	}
	private double calculateBill() {
		//logic to calclate bill
		return 1000.0;
	}
 	public String getTable() {
		return tableID;
	}
	public void setTable(String table) {
		this.tableID = table;
	}
	public Map<String, List<Dish>> getOrderList() {
		return orderList;
	}
	public void setOrderList(Map<String, List<Dish>> orderList) {
		this.orderList = orderList;
	}
	public double getBillForThisOrder() {
		return billForThisOrder;
	}
	public void setBillForThisOrder(double billForThisOrder) {
		this.billForThisOrder = billForThisOrder;
	}
	public String getOrderID() {
		return OrderID;
	}
	public void setOrderID(String orderID) {
		OrderID = orderID;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
}
class Waiter {
	private String name;
	private String waiterID;
	public Waiter(String name, String waiterID) {
		super();
		this.name = name;
		this.waiterID = waiterID;
	}
	public String placeOrder(Order order) {
		System.out.println(name + " has placed your order with order ID " + order.getOrderID());
		return null;
	}
}
class BranchManager {
	private String branchID;
	private Address address;
	private Map<String, Table> allTables;
	private Map<String, Waiter> waiters;
	private Menu menu;
	public BranchManager(String branchID, Address address, Map<String, Table> allTables, Map<String, Waiter> waiters, Menu menu) {
		super();
		this.branchID = branchID;
		this.address = address;
		this.allTables = allTables;
		this.waiters = waiters;
		this.menu = menu;
	}
	public String placeOrder(Order order) {
		Waiter waiter = waiters.get("W1");
		return waiter.placeOrder(order);
	}
	public String getBranchID() {
		return branchID;
	}
	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Map<String, Table> getAllTables() {
		return allTables;
	}
	public void setAllTables(Map<String, Table> allTables) {
		this.allTables = allTables;
	}
	public Map<String, Waiter> getWaiters() {
		return waiters;
	}
	public void setWaiters(Map<String, Waiter> waiters) {
		this.waiters = waiters;
	}
	
}
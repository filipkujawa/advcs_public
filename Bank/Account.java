public class Account implements Comparable<Account> {
    String first_name;
    String last_name;
    int pin;
    double balance;

    public Account(String first_name, String last_name, double balance, int pin) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.pin = pin;
        this.balance = balance;
    }

    public Account(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.pin = (int) Math.floor(1000 + Math.random() * 9000);
        this.balance = Math.random() * 100000;
    }

    public int compareTo(Account other) {
        if (this.last_name.compareTo(other.last_name) < 0) {
            return -1;
        } else if (this.last_name.compareTo(other.last_name) > 0) {
            return 1;
        } else {
            if (this.first_name.compareTo(other.first_name) < 0) {
                return -1;
            } else if (this.first_name.compareTo(other.first_name) > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    

    public String toString() {
        return first_name + " " + last_name;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public int getPin() {
        return pin;
    }

    public boolean checkPin(int pin) {
        return this.pin == pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
        }
    }



}

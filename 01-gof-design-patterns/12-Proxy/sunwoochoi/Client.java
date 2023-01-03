/* Client */
public class Client {
  public static void main(String[] args) {
    Transaction transaction = new ThirdTransaction();
    TransactionProxy proxy = new TransactionProxy(transaction);
    System.out.println(proxy.getUserList()); 
    // Get user list from third party library
    // [Sam, Eric, Mina, Tolu, Smit, Sunwoo]
    System.out.println(proxy.getUserList());
    // [Sam, Eric, Mina, Tolu, Smit, Sunwoo]
    System.out.println(proxy.getTransactionHistory("Sam"));
    // Get transaction history of user Sam from third party library
    // [Date: Sun Oct 02 16:50:06 PDT 2022; Amount:$ 98, Date: Sun Oct 02 16:50:06 PDT 2022; Amount:$ 47]
    System.out.println(proxy.getTransactionHistory("Sam"));
    // [Date: Sun Oct 02 16:50:06 PDT 2022; Amount:$ 98, Date: Sun Oct 02 16:50:06 PDT 2022; Amount:$ 47]
    System.out.println(proxy.getTransactionHistory("Eric"));
    // Get transaction history of user Eric from third party library
    // [Date: Sun Oct 02 16:50:06 PDT 2022; Amount:$ 31, Date: Sun Oct 02 16:50:06 PDT 2022; Amount:$ 17, Date: Sun Oct 02 16:50:06 PDT 2022; Amount:$ 99]
  }
}
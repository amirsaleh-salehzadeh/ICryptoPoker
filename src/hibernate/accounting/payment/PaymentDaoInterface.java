package hibernate.accounting.payment;
import common.accounting.PaymentENT;
import common.accounting.PaymentLST;

public interface PaymentDaoInterface {

	public PaymentENT savePayment(PaymentENT ent);

	public PaymentLST  getPaymentLST(PaymentLST searchENT);

	public PaymentLST getUserPayments(PaymentLST searchENT);

}

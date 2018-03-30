package hibernate.accounting.payment;
import common.accounting.payment.PaymentENT;
import common.accounting.payment.PaymentLST;

public interface PaymentDaoInterface {

	public PaymentENT savePayment(PaymentENT ent);

	public PaymentLST  getPaymentLST(PaymentLST searchENT);

	public PaymentLST getUserPayments(PaymentLST searchENT);

}

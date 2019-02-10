package database_console;

/**

 @author A.Smith
 */
public class approvalCodeHandler {

    private String responseText;
    private String code;
    private boolean approved = false;

    approvalCodeHandler(String cardType, String code) {
        this.code = code;
        switch (cardType)
        {
            case "DISCOVER":
                discover();
                break;
            case "AMERICAN EXPRESS":
                americanExpress();
                break;
            case "MASTERCARD":
                masterCard();
                break;
            case "VISA":
                visa();
                break;
            default:
                unknown();
        }
    }

    public boolean isApproved() {
        return approved;
    }

    private void discover() {
        approved = false;
        System.out.println(code);
        switch (code)
        {
            case "00":
                responseText = "APPROVED";
                approved = true;
                break;
            case "01":
                responseText = "NEEDS TO CALL";
                break;
            case "03":
                responseText = "TERM ID ERROR";
                break;
            case "04":
                responseText = "HOLD-CALL";
                break;
            case "05":
                responseText = "DECLINED";
                break;
            case "07":
                responseText = "HOLD-CALL";
                break;
            case "08":
                responseText = "HONOR WITH ID";
                break;
            case "10":
                responseText = "APPROVED";
                approved = true;
                break;
            case "12":
                responseText = "INVALID TRANS";
                break;
            case "13":
                responseText = "AMOUNT ERROR";
                break;
            case "14":
                responseText = "CARD NO. ERROR";
                break;
            case "15":
                responseText = "NO SUCH ISSUER";
                break;
            case "19":
                responseText = "RE ENTER";
                break;
            case "30":
                responseText = "NEEDS TO CALL";
                break;
            case "31":
                responseText = "NEEDS TO CALL";
                break;
            case "35":
                responseText = "NEEDS TO CALL";
                break;
            case "36":
                responseText = "NEEDS TO CALL";
                break;
            case "37":
                responseText = "NEEDS TO CALL";
                break;
            case "38":
                responseText = "PIN EXCEEDED";
                break;
            case "39":
                responseText = "NO CREDIT ACCT";
                break;
            case "40":
                responseText = "NEEDS TO CALL";
                break;
            case "41":
                responseText = "HOLD-CALL";
                break;
            case "51":
                responseText = "DECLINED";
                break;
            case "53":
                responseText = "NO SAVE ACCOUNT";
                break;
            case "55":
                responseText = "WRONG PIN";
                break;
            case "56":
                responseText = "NO CARD RECORD";
                break;
            case "57":
                responseText = "SERVICE NOT ALLOWED";
                break;
            case "58":
                responseText = "SERVICE NOT ALLOWED";
                break;
            case "59":
                responseText = "DECLINED";
                break;
            case "60":
                responseText = "NEEDS TO CALL";
                break;
            case "61":
                responseText = "DECLINED";
                break;
            case "64":
                responseText = "NEEDS TO CALL";
                break;
            case "65":
                responseText = "DECLINED";
                break;
            case "66":
                responseText = "NEEDS TO CALL";
                break;
            case "67":
                responseText = "HOLD-CALL";
                break;
            case "77":
                responseText = "NEEDS TO CALL";
                break;
            case "78":
                responseText = "NO ACCOUNT";
                break;
            case "87":
                responseText = "NO REPLY";
                break;
            case "91":
                responseText = "NO REPLY";
                break;
            case "92":
                responseText = "INVALID ROUTING";
                break;
            case "94":
                responseText = "NEEDS TO CALL";
                break;
            default:
                responseText = "DECLINED, UNKNOWN ERROR";
        }

    }

    private void masterCard() {
        approved = false;
        System.out.println(code);
        switch (code)
        {
            case "00":
                responseText = "APPROVED";
                approved = true;
                break;
            case "01":
                responseText = "NEEDS TO CALL";
                break;
            case "02":
                responseText = "NEEDS TO CALL";
                break;
            case "03":
                responseText = "TERM ID ERROR";
                break;
            case "04":
                responseText = "HOLD-CALL";
                break;
            case "05":
                responseText = "DECLINED";
                break;
            case "06":
                responseText = "ERROR";
                break;
            case "07":
                responseText = "HOLD-CALL";
                break;
            case "12":
                responseText = "INVALID TRANS";
                break;
            case "13":
                responseText = "AMOUNT ERROR";
                break;
            case "14":
                responseText = "CARD NO. ERROR";
                break;
            case "15":
                responseText = "NO SUCH ISSUER";
                break;
            case "19":
                responseText = "RE ENTER";
                break;
            case "21":
                responseText = "NO ACTION TAKEN";
                break;
            case "28":
                responseText = "NO REPLY";
                break;
            case "41":
                responseText = "HOLD-CALL";
                break;
            case "43":
                responseText = "HOLD-CALL";
                break;
            case "51":
                responseText = "DECLINED";
                break;
            case "52":
                responseText = "NO CHECK ACCOUNT";
                break;
            case "53":
                responseText = "NO SAVE ACCOUNT";
                break;
            case "54":
                responseText = "EXPIRED CARD";
                break;
            case "55":
                responseText = "WRONG PIN";
                break;
            case "57":
                responseText = "SERV NOT ALLOWED";
                break;
            case "58":
                responseText = "SERV NOT ALLOWED";
                break;
            case "61":
                responseText = "DECLINED";
                break;
            case "62":
                responseText = "DECLINED";
                break;
            case "63":
                responseText = "SEC VIOLATION";
                break;
            case "65":
                responseText = "DECLINED";
                break;
            case "75":
                responseText = "PIN EXCEEDED";
                break;
            case "76":
                responseText = "NO ACTION TAKEN";
                break;
            case "77":
                responseText = "NO ACTION TAKEN";
                break;
            case "80":
                responseText = "DATE ERROR";
                break;
            case "81":
                responseText = "ENCRYPTION ERROR";
                break;
            case "82":
                responseText = "CASHBACK NOT APP";
                break;
            case "83":
                responseText = "CANT VERIFY PIN";
                break;
            case "91":
                responseText = "NO REPLY";
                break;
            case "92":
                responseText = "INVALID ROUTING";
                break;
            case "93":
                responseText = "DECLINED";
                break;
            case "94":
                responseText = "DUP TRANS";
                break;
            case "96":
                responseText = "SYSTEM ERROR";
                break;
            case "EB":
                responseText = "CHECK DIGIT ERROR";
                break;
            case "ER":
                responseText = "ERROR";
                break;
            case "N3":
                responseText = "CASHBACK NOT AVAIL";
                break;
            case "N4":
                responseText = "DECLINE";
                break;
            case "N7":
                responseText = "CVV2 MISMATCH";
                break;
            case "TO":
                responseText = "TIMEOUT";
                break;
            default:
                responseText = "DECLINED, UNKNOWN ERROR";
        }
    }

    private void americanExpress() {
        approved = false;
        System.out.println(code);
        switch (code)
        {
            case "00":
                responseText = "APPROVED";
                approved = true;
                break;
            case "01":
                responseText = "APPROVED WITH ID";
                approved = true;
                break;
            case "05":
                responseText = "DECLINED";
                break;
            case "54":
                responseText = "EXPIRED CARD";
                break;
            case "03":
                responseText = "TERM ID ERROR";
                break;
            case "13":
                responseText = "AMOUNT ERROR";
                break;
            case "78":
                responseText = "NO ACCOUNT";
                break;
            case "57":
                responseText = "SERV NOT ALLOWED";
                break;
            case "N7":
                responseText = "CID MISMATCH";
                break;
            case "04":
                responseText = "HOLD-CALL";
                break;
            default:
                responseText = "DECLINED, UNKNOWN ERROR";
        }
    }

    private void visa() {
        approved = false;
        System.out.println(code);
        switch (code)
        {
            case "00":
                responseText = "APPROVED";
                approved = true;
                break;
            case "01":
                responseText = "NEEDS TO CALL";
                break;
            case "02":
                responseText = "NEEDS TO CALL";
                break;
            case "03":
                responseText = "TERM ID ERROR";
                break;
            case "04":
                responseText = "HOLD-CALL";
                break;
            case "05":
                responseText = "DECLINED";
                break;
            case "06":
                responseText = "ERROR";
                break;
            case "07":
                responseText = "HOLD-CALL";
                break;
            case "12":
                responseText = "INVALID TRANS";
                break;
            case "13":
                responseText = "AMOUNT ERROR";
                break;
            case "14":
                responseText = "CARD NO. ERROR";
                break;
            case "15":
                responseText = "NO SUCH ISSUER";
                break;
            case "19":
                responseText = "RE ENTER";
                break;
            case "21":
                responseText = "NO ACTION TAKEN";
                break;
            case "28":
                responseText = "NO REPLY";
                break;
            case "41":
                responseText = "HOLD-CALL";
                break;
            case "43":
                responseText = "HOLD-CALL";
                break;
            case "51":
                responseText = "DECLINED";
                break;
            case "52":
                responseText = "NO CHECK ACCOUNT";
                break;
            case "53":
                responseText = "NO SAVE ACCOUNT";
                break;
            case "54":
                responseText = "EXPIRED CARD";
                break;
            case "55":
                responseText = "WRONG PIN";
                break;
            case "57":
                responseText = "SERVICE NOT ALLOWED";
                break;
            case "58":
                responseText = "SERVICE NOT ALLOWED";
                break;
            case "61":
                responseText = "DECLINED";
                break;
            case "62":
                responseText = "DECLINED";
                break;
            case "63":
                responseText = "SEC VIOLATION";
                break;
            case "65":
                responseText = "DECLINE";
                break;
            case "75":
                responseText = "PIN EXCEEDED";
                break;
            case "76":
                responseText = "NO ACTION TAKEN";
                break;
            case "77":
                responseText = "NO ACTION TAKEN";
                break;
            case "80":
                responseText = "DATE ERROR";
                break;
            case "81":
                responseText = "ENCRYPTION ERROR";
                break;
            case "82":
                responseText = "CVV ERROR";
                break;
            case "83":
                responseText = "CANT VERIFY PIN";
                break;
            case "91":
                responseText = "NO REPLY";
                break;
            case "92":
                responseText = "INVALID ROUTING";
                break;
            case "93":
                responseText = "DECLINE";
                break;
            case "94":
                responseText = "NEEDS TO CALL";
                break;
            case "96":
                responseText = "SYSTEM ERROR";
                break;
            case "EB":
                responseText = "CHECK DIGIT ERROR";
                break;
            case "ER":
                responseText = "ERROR";
                break;
            case "N3":
                responseText = "CASHBACK NOT AVAIL";
                break;
            case "N4":
                responseText = "DECLINE";
                break;
            case "N7":
                responseText = "CVV2 MISMATCH";
                break;
            case "TO":
                responseText = "TIMEOUT";
                break;
            default:
                responseText = "DECLINED, UNKNOWN ERROR";
        }
    }

    private void unknown() {
        System.out.println(code);
        System.out.println("SPECIAL CARD");
        approved = false;
        switch (code)
        {
            case "00":
                responseText = "APPROVED";
                approved = true;
                break;
            case "01":
                responseText = "NEEDS TO CALL";
                break;
            case "02":
                responseText = "NEEDS TO CALL";
                break;
            case "03":
                responseText = "INVALID MERCHANT";
                break;
            case "04":
                responseText = "HOLD-CALL PICKUP CARD";
                break;
            case "05":
                responseText = "DECLINED DO NOT HONOR";
                break;
            case "06":
                responseText = "DECLINED ERROR";
                break;
            case "07":
                responseText = "PICKUP CARD SPECIAL CONDITION";
                break;
            case "08":
                responseText = "HONOR WITH ID";
                break;
            case "10":
                responseText = "APPROVAL FOR PARTIAL AMOUNT";
                approved = true;
                break;
            case "12":
                responseText = "INVALID TRANS";
                break;
            case "13":
                responseText = "AMOUNT ERROR";
                break;
            case "14":
                responseText = "CARD NO. ERROR";
                break;
            case "15":
                responseText = "NO SUCH ISSUER";
                break;
            case "19":
                responseText = "RE ENTER CARD";
                break;
            case "21":
                responseText = "NO ACTION TAKEN";
                break;
            case "25":
                responseText = "UNABLE TO LOCATE";
                break;
            case "28":
                responseText = "NO REPLY";
                break;
            case "30":
                responseText = "FORMAT ERROR";
                break;
            case "31":
                responseText = "BANK NOT SUPPORTED";
                break;
            case "33":
                responseText = "EXPIRED CARD";
                break;
            case "34":
                responseText = "SUSPECTED FRAUD CALL ISSUER";
                break;
            case "35":
                responseText = "CARD ACCEPTOR CONTACT ACQUIER";
                break;
            case "36":
                responseText = "RESTRICTED CARD";
                break;
            case "37":
                responseText = "CALL CARD ACCEPTOR";
                break;
            case "38":
                responseText = "ALLOWABLE PIN TRIES EXCEEDED";
                break;
            case "39":
                responseText = "NO CREDIT AMOUNT";
                break;
            case "40":
                responseText = "REQUESTED FUNCTION NOT SUPPORTED";
                break;
            case "41":
                responseText = "PICKUP CARD (lost card)";
                break;
            case "43":
                responseText = "PICKUP CARD (stolen card)";
                break;
            case "51":
                responseText = "DECLINED";
                break;
            case "52":
                responseText = "NO CHECKING ACCOUNT";
                break;
            case "53":
                responseText = "NO SAVINGS ACCOUNT";
                break;
            case "54":
                responseText = "EXPIRED CARD";
                break;
            case "55":
                responseText = "WRONG PIN";
                break;
            case "56":
                responseText = "NO CARD RECORD";
                break;
            case "57":
                responseText = "SERVICE NOT ALLOWED";
                break;
            case "58":
                responseText = "TRANSACTION NOT ALLOWED";
                break;
            case "59":
                responseText = "SUSPECTED FRAUD";
                break;
            case "60":
                responseText = "CARD ACCEPTOR CONTACT ACQUIRER";
                break;
            case "61":
                responseText = "ACTIVITY AMT LIMIT EXCEEDED";
                break;
            case "62":
                responseText = "RESTRICTED CARD";
                break;
            case "63":
                responseText = "SECURITY VIOLATION";
                break;
            case "64":
                responseText = "ORIGINAL AMT INCORRECT";
                break;
            case "65":
                responseText = "ACTIVITY COUNT LIMIT EXCEEDED";
                break;
            case "66":
                responseText = "CARD ACCEPTOR CALL SECURITY DEPT";
                break;
            case "67":
                responseText = "HARD CAPTURE ATM PICKUP";
                break;
            case "68":
                responseText = "RESPONSE RECEIVED TOO LATE";
                break;
            case "75":
                responseText = "PIN EXCEEDED";
                break;
            case "76":
                responseText = "NO ACTION TAKEN";
                break;
            case "78":
                responseText = "INVALID ACCOUNT";
                break;
            case "79":
                responseText = "ALREADY REVERSED";
                break;
            case "80":
                responseText = "INVALID DATE";
                break;
            case "81":
                responseText = "ENCRYPTION ERROR";
                break;
            case "83":
                responseText = "CANT VERIFY PIN";
                break;
            case "85":
                responseText = "ACCOUNT VERIFICATION REQUIRED";
                break;
            case "86":
                responseText = "CANNOT VERIFY PIN";
                break;
            case "87":
                responseText = "NETWORK UNAVIABLE";
                break;
            case "91":
                responseText = "ISSUER UNAVIABLE";
                break;
            case "92":
                responseText = "INVALID ROUTING";
                break;
            case "93":
                responseText = "DECLINED VIOLATION OF LAW";
                break;
            case "94":
                responseText = "DUP TRANS";
                break;
            case "96":
                responseText = "SYSTEM ERROR";
                break;
            case "99":
                responseText = "CARD NETWORK FAULT ERR";
                break;
            case "B1":
                responseText = "OPEN BATCH NOT FOUND";
                break;
            case "B2":
                responseText = "INVALID BATCH";
                break;
            case "E1":
                responseText = "INVALID OR UNSUPPORTED SEC";
                break;
            case "E2":
                responseText = "AVS DATA REQUIRED";
                break;
            case "E3":
                responseText = "CVV2 REQUIRED";
                break;
            default:
                responseText = "DECLINED, UNKNOWN ERROR";
        }
    }

    public String getResponseText() {
        return responseText;
    }
}

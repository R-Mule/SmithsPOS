package database_console;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import java.time.LocalDateTime;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**

 @author R-Mule
 */
public class DateRangeSelector {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean validDates = false;

    public DateRangeSelector() {
        String startTemp, endTemp;
        LocalDateTime start, end;

        DatePickerSettings datePickerSettings = new DatePickerSettings();
        datePickerSettings.setFormatForDatesBeforeCommonEra("MM/dd/yyyy");
        datePickerSettings.setFormatForDatesCommonEra("MM/dd/yyyy");
        datePickerSettings.setAllowEmptyDates(false);

        DatePicker startDatePicker = new DatePicker(datePickerSettings);
        startDatePicker.setDateToToday();
        startDatePicker.setSize(200, 200);
        startDatePicker.setVisible(true);

        DatePickerSettings datePickerSettings2 = new DatePickerSettings();
        datePickerSettings2.setFormatForDatesBeforeCommonEra("MM/dd/yyyy");
        datePickerSettings2.setFormatForDatesCommonEra("MM/dd/yyyy");
        datePickerSettings2.setAllowEmptyDates(false);

        DatePicker endDatePicker = new DatePicker(datePickerSettings2);
        endDatePicker.setDateToToday();
        endDatePicker.setSize(200, 200);
        endDatePicker.setVisible(true);

        JFrame textInputFrame = new JFrame("");
        JTextField field1 = new JTextField();
        field1.addAncestorListener(new RequestFocusListener());
        Object[] message =
        {
            "Start Date:",
            startDatePicker,
            "End Date:",
            endDatePicker
        };
        int option = JOptionPane.showConfirmDialog(textInputFrame, message, "Please select date range (INCLUSIVE)", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
        {
            startTemp = startDatePicker.getText();
            endTemp = endDatePicker.getText();
            if (!startTemp.isEmpty() && !endTemp.isEmpty())
            {
                start = startDatePicker.getDate().atStartOfDay();
                end = endDatePicker.getDate().atStartOfDay();

                if (!start.isAfter(end))
                {//Dates are valid and are in proper order.
                    startDate = start;
                    //endDate = end.plusHours(23);
                    // endDate = endDate.plusMinutes(55);
                    endDate = end;
                    validDates = true;
                    // System.out.println(startDate.getHour()+":::"+endDate.getHour());

                }
                else
                {
                    JFrame message1 = new JFrame("");
                    JOptionPane.showMessageDialog(message1, "Start date must be before end date!");
                }
            }
            else
            {
                JFrame message1 = new JFrame("");
                JOptionPane.showMessageDialog(message1, "Date ranges cannot be blank!");
            }
        }
    }

    public boolean validDates() {
        return validDates;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}

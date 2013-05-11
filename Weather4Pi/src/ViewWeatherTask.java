import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimerTask;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.pi4j.component.lcd.LCD;

import weather4pi.lcd.LcdFactory;
import weather4pi.openweathermap.jaxb.Forecast;
import weather4pi.openweathermap.jaxb.Temperature;
import weather4pi.openweathermap.jaxb.Weatherdata;


public class ViewWeatherTask extends TimerTask {

	final protected static URL WEATHER_DATA_URL;
	
	static {
		URL urlResult;
		try{ 
			urlResult = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Munich&cnt=2&mode=xml&lang=de&units=metric");
			
		}catch (MalformedURLException e) {
			urlResult = null;
		}
		WEATHER_DATA_URL = urlResult;
	}

	@Override
	public void run() {
		Weatherdata weather = null;
		try {
			weather = retrieveWeatherData();
		} catch (JAXBException e) {
			System.out.println(e);
		}
		
		if(weather != null && weather.getForecasts().length == 2){
			Forecast forecast = weather.getForecasts()[1];
			Temperature temperature = forecast.getTemperature();
			String dayOfWeek = forecast.getDate().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.GERMAN);
			String line1 = String.format("%s:   %02.0f - %02.0f  C", 
											String.format("%2s",dayOfWeek), //2 signs of the day, "Mo", "Di", "Mi"...
											temperature.getMinimum(),
											temperature.getMaximum());
			String line2 = forecast.getSymbol().getDescription();
			System.out.println(line1 + " - length: " + line1.length());
			System.out.println(line2 + " - length: " + line2.length());
		}
	}
	
	protected static Weatherdata retrieveWeatherData() throws JAXBException {
		Weatherdata result = null;
		
		JAXBContext context = JAXBContext.newInstance(Weatherdata.class);

		Unmarshaller unmarshaller = context.createUnmarshaller();

		result = (Weatherdata) unmarshaller.unmarshal(WEATHER_DATA_URL);
		return result;
	}

	protected static void displayLcdMessage(String[] lines) {
		LCD lcd = LcdFactory.createWeather4PiLcd();
		if(lcd.getRowCount() >= lines.length) {
			for(int lineNr=0; lineNr < lines.length; lineNr++) {
				String line = lines[lineNr];
				if(line != null && line.length() >= lcd.getColumnCount()) {
					lcd.write(lineNr + 1, line);
				}
			}
		}
	}
}
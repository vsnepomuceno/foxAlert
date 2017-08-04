package util;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.springframework.stereotype.Component;

@Component
@ManagedBean(name = "foxAlertModel", eager = true)
@RequestScoped
public class FoxAlertModel {

	public void setParameters(double sup, double inf, int maxCounter) {
		
		FoxAlertParameters.limiteSup = sup;
		FoxAlertParameters.limiteInf = inf;
		FoxAlertParameters.max_counter = maxCounter;
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"Parametros Atualizados!", ""));
		
	}
}

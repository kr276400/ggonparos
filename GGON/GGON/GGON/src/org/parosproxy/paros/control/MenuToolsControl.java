package org.parosproxy.paros.control;

import javax.swing.JOptionPane;

import org.apache.commons.configuration.ConfigurationException;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.view.OptionsDialog;
import org.parosproxy.paros.view.View;

public class MenuToolsControl {
	
	private View view = null;
	private Model model = null;
	private Control control = null;
	
	public MenuToolsControl() {
		// 만약 값이 주어지지 않았다면, 안에있는 MVC를 이용해라
		/*
		 * MVC는 패턴인데 Model - View - Controller 의 줄임말이여,
		 * 즉, 데이터(Model)을 사용자가 보고(View) 사용자가 보기위해서 데이터를 가공 즉, 조작하는(Controller)
		 * 이러한 작업을 시행하는 패턴이야
		 */
	    view = View.getSingleton();
	    model = Model.getSingleton();
	    control = Control.getSingleton();
	}
	
	public MenuToolsControl(Model model, View view, Control control) {
		// 확실한 클래스의 관리자를 이용하는게 짱
	    this.model = model;
	    this.view = view;
	    this.control = control;
	}
	
	public void options() {
	    OptionsDialog dialog = view.getOptionsDialog("옵션");
	    dialog.initParam(model.getOptionsParam());
	    // 사용자가 입력한 파라미터 값을 초기화 하는거 같어 혹은 파라미터 값을 초기화 - initParam()
		int result = dialog.showDialog(false);
		if (result == JOptionPane.OK_OPTION) {
		    try {
                model.getOptionsParam().getConfig().save();
            } catch (ConfigurationException e) {
                e.printStackTrace();
                view.showWarningDialog("옵션을 저장하는 중에 오류가 발생했습니다.");
                return;
            }
		    control.getProxy().stopServer();
		    control.getProxy().startServer();
		}
	}
}

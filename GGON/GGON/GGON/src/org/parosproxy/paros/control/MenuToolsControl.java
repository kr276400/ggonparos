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
		// ���� ���� �־����� �ʾҴٸ�, �ȿ��ִ� MVC�� �̿��ض�
		/*
		 * MVC�� �����ε� Model - View - Controller �� ���Ӹ��̿�,
		 * ��, ������(Model)�� ����ڰ� ����(View) ����ڰ� �������ؼ� �����͸� ���� ��, �����ϴ�(Controller)
		 * �̷��� �۾��� �����ϴ� �����̾�
		 */
	    view = View.getSingleton();
	    model = Model.getSingleton();
	    control = Control.getSingleton();
	}
	
	public MenuToolsControl(Model model, View view, Control control) {
		// Ȯ���� Ŭ������ �����ڸ� �̿��ϴ°� ¯
	    this.model = model;
	    this.view = view;
	    this.control = control;
	}
	
	public void options() {
	    OptionsDialog dialog = view.getOptionsDialog("�ɼ�");
	    dialog.initParam(model.getOptionsParam());
	    // ����ڰ� �Է��� �Ķ���� ���� �ʱ�ȭ �ϴ°� ���� Ȥ�� �Ķ���� ���� �ʱ�ȭ - initParam()
		int result = dialog.showDialog(false);
		if (result == JOptionPane.OK_OPTION) {
		    try {
                model.getOptionsParam().getConfig().save();
            } catch (ConfigurationException e) {
                e.printStackTrace();
                view.showWarningDialog("�ɼ��� �����ϴ� �߿� ������ �߻��߽��ϴ�.");
                return;
            }
		    control.getProxy().stopServer();
		    control.getProxy().startServer();
		}
	}
}

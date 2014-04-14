package fenixDomainBrowser.client;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import fenixDomainBrowser.shared.ClassBean;
import fenixDomainBrowser.shared.FDBState;

public class ClassSearch extends Composite {

    private static ClassSearchUiBinder uiBinder = GWT.create(ClassSearchUiBinder.class);
    public List<ClassBean> allClasses;
    @UiField(provided = true)
    CellList<ClassBean> list = new CellList<ClassBean>(new ClassBean.ClassBeanCell(), ClassBean.KEY_PROVIDER);
    List<ClassBean> allClassBeans;
    @UiField
    ScrollPanel scrollPanel;

    @UiField
    TextBox searchField;

    interface ClassSearchUiBinder extends UiBinder<Widget, ClassSearch> {
    }

    public SingleSelectionModel<ClassBean> selectionModel;

    public ClassSearch() {
	initWidget(uiBinder.createAndBindUi(this));
	list.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
	selectionModel = new SingleSelectionModel<ClassBean>(ClassBean.KEY_PROVIDER);
	list.setSelectionModel(selectionModel);
	selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	    public void onSelectionChange(SelectionChangeEvent event) {
		ClassBean b = selectionModel.getSelectedObject();
		FDBState bean = Interface.currentState;
		History.newItem("seeClass:" + b.getId());
		Interface.refresh();
	    }
	});
	searchField.addKeyPressHandler(new KeyPressHandler() {

	    @Override
	    public void onKeyPress(KeyPressEvent event) {
		String text = searchField.getText();
		if (!searchField.getText().equals("")) {
		    List<ClassBean> l = allClasses;
		    TreeMap<Double, ClassBean> data = new TreeMap<Double, ClassBean>();
		    for (ClassBean classBean : l) {
			double score = QuickSilverSearchAlgorithm.score(classBean.getQualifiedName(), text, 0);
			data.put(score, classBean);
		    }
		    setData(new ArrayList<ClassBean>(data.values()));
		}else{
		    setData(allClasses);
		}
	    }

	});
    }

    public void setData(List<ClassBean> a) {
	list.setRowCount(a.size());
	list.setRowData(a);
    }

    public ClassBean getClassBeanForName(String name) {
	for (ClassBean cl : allClasses) {
	    if (cl.getId().equals(name)) {
		return cl;
	    }
	}
	return null;
    }

}

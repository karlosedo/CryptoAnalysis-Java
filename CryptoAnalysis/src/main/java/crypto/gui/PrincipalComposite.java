package crypto.gui;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import crypto.console.AnalysisCoinGecko;
import crypto.console.AnalysisConsole;
import crypto.console.AnalysisObject;
import crypto.persistence.DBCrypto;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

public class PrincipalComposite extends org.eclipse.swt.widgets.Composite {
	private Text textInvest;
	private Text textAdds;
	private Text text;
	private Text txtResultado;
	private Text textBuy;
	private Text textSale;
	private Text textFile;
	DateTime dateTimeStart;
	DateTime dateTimeEnd;
	Combo option;
	Label labelError;
	private Label lblGananciaIdeal;
	private Text textIdeal;
	private Button checkGck;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PrincipalComposite(Shell parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		Label lblInversinInicial = new Label(this, SWT.NONE);
		lblInversinInicial.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblInversinInicial.setText("Inversi\u00F3n Inicial");
		
		textInvest = new Text(this, SWT.BORDER);
		textInvest.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textInvest.append("1000");
		
		Label lblAportesMensuales = new Label(this, SWT.NONE);
		lblAportesMensuales.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAportesMensuales.setText("Aportes mensuales");
		
		textAdds = new Text(this, SWT.BORDER);
		textAdds.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textAdds.append("0");
		
		Label lblOpcion = new Label(this, SWT.NONE);
		lblOpcion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOpcion.setText("Opci\u00F3n");
		
		option = new Combo(this, SWT.NONE);
		option.setItems(new String[] {"Estricto", 
										"Mejor opci\u00F3n", 
										"Solo ganancias", 
										"Buscando recuperación", 
										"Ganancia Ideal", 
										"Recojo ganancias", 
										"Ganancia porcentual", 
										"Mensual y HODL",
										"Aporte diario",
										"Recompra",
										"HODL"});
		option.select(0);
		option.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblArchivo = new Label(this, SWT.NONE);
		lblArchivo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblArchivo.setText("Archivo");
		
		textFile = new Text(this, SWT.BORDER);
		textFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textFile.append("btc");
		
		Label lblDaCompra = new Label(this, SWT.NONE);
		lblDaCompra.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDaCompra.setText("D\u00EDa compra");
		
		textBuy = new Text(this, SWT.BORDER);
		textBuy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textBuy.setText("1");
		
		Label lblDaVenta = new Label(this, SWT.NONE);
		lblDaVenta.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDaVenta.setText("D\u00EDa venta");
		
		textSale = new Text(this, SWT.BORDER);
		textSale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textSale.setText("28");
		
		Label lblFechaInicio = new Label(this, SWT.NONE);
		lblFechaInicio.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFechaInicio.setText("Fecha Inicio");
		
		dateTimeStart = new DateTime(this, SWT.BORDER);
		
		Label lblFechaFin = new Label(this, SWT.NONE);
		lblFechaFin.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFechaFin.setText("Fecha fin");
		
		dateTimeEnd = new DateTime(this, SWT.BORDER);
		
		lblGananciaIdeal = new Label(this, SWT.NONE);
		lblGananciaIdeal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGananciaIdeal.setText("Ganancia Ideal (%)($)");
		
		textIdeal = new Text(this, SWT.BORDER);
		textIdeal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textIdeal.setText("0");;
		
		Label lblCoinGecko = new Label(this, SWT.NONE);
		lblCoinGecko.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCoinGecko.setText("CoinGecko");
		
		checkGck = new Button(this, SWT.CHECK);
		checkGck.setSelection(true);
		
		new Label(this, SWT.NONE);
		
		labelError = new Label(this, SWT.NONE);
		labelError.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		labelError.setText("");
//		labelError.setVisible(true);
		new Label(this, SWT.NONE);
		
		Button btnEjecutar = new Button(this, SWT.NONE);
		GridData gd_btnEjecutar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnEjecutar.widthHint = 80;
		btnEjecutar.setLayoutData(gd_btnEjecutar);
		btnEjecutar.setText("Ejecutar");
		executeBtn(btnEjecutar);
		
		txtResultado = new Text(this, SWT.NONE);
		txtResultado.setText("Resultado");
		txtResultado.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_scrolledComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_scrolledComposite.heightHint = 300;
		gd_scrolledComposite.widthHint = 642;
		scrolledComposite.setLayoutData(gd_scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		text = new Text(scrolledComposite, SWT.BORDER | SWT.V_SCROLL);
		text.setEditable(false);
		scrolledComposite.setContent(text);
		scrolledComposite.setMinSize(text.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//		new Label(this, SWT.NONE);
		
		Button btnReset = new Button(this, SWT.NONE);
		btnReset.setText("Reset");
		executeBtnReset(btnReset);
		
		Button btnLimpiar = new Button(this, SWT.NONE);
		btnLimpiar.setText("Limpiar");
		executeBtnClean(btnLimpiar);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	protected void executeBtn(Button btn) {
		btn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				labelError.setText("");
				text.setText("");
				processAnalysis();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

				text.setText("Prueba2\n");
				text.setText("WidgetDefaultSelected");
				
			}
		});
	}
	
	protected void executeBtnReset(Button btn) {
		btn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				labelError.setText("");
				text.setText("");
				DBCrypto db = new DBCrypto();
				text.setText(db.cleanChargedInfo());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

				text.setText("Prueba2\n");
				text.setText("WidgetDefaultSelected");
				
			}
		});
	}
	
	protected void executeBtnClean(Button btn) {
		btn.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				text.setText("");
//				ab.set
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

				text.setText("Prueba2\n");
				text.setText("WidgetDefaultSelected");
				
			}
		});
	}
	
	protected void processAnalysis() {
		try {
			AnalysisConsole ac;
			if (checkGck.getSelection()) 
				ac = new AnalysisCoinGecko();
			else 
				ac = new AnalysisObject();
			ac.setInitInvest(Double.parseDouble(textInvest.getText()));
			ac.setAddedInvest(Double.parseDouble(textAdds.getText()));
			ac.setOption(option.getSelectionIndex());
			ac.setBuyDay(Integer.parseInt(textBuy.getText()));
			ac.setSaleDay(Integer.parseInt(textSale.getText()));
			ac.setInitDateStr(dateTimeStart.getYear()+"-"+(dateTimeStart.getMonth()+1)+"-"+dateTimeStart.getDay());
			ac.setEndDateStr(dateTimeEnd.getYear()+"-"+(dateTimeEnd.getMonth()+1)+"-"+dateTimeEnd.getDay());
			ac.setToken(textFile.getText());
			ac.setIdeal(Double.parseDouble(textIdeal.getText()));
			ac.process();
			text.append(ac.getMessages().toString());

		} catch (NumberFormatException e) {
			labelError.setText("Revise formato numérico");
		} catch (Exception e) {
			e.printStackTrace();
			labelError.setText(e.getMessage());;
		}
	}
}

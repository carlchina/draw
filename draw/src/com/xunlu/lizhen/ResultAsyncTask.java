package com.xunlu.lizhen;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

/**����ʹ�õ�AsyncTask */
public abstract class ResultAsyncTask <T> extends AsyncTask<Void, Void, T>{
	private ProgressDialog pd;
	private Runnable onSucRun;
	private Runnable onFailRun;
	private Context context;
	private boolean isSucToast=false;
	private boolean isFailToast=true;
	private String sucToast;
	private String failToast;
	private boolean isDismissPd=true;
	public ResultAsyncTask(Context context){
		this.context=context;
	}
	public ResultAsyncTask<T> setProgressDialog(ProgressDialog pd){
		this.pd=pd;
		return this;
	}
	public ResultAsyncTask<T> setProgressDialogDefault(){
		return setProgressDialogDefault(true);
	}
	public ResultAsyncTask<T> setProgressDialogDefault(boolean cancleAble){
		ProgressDialog pd=new ProgressDialog(context);
		pd.setMessage("���Ժ�...");
		pd.setCanceledOnTouchOutside(false);
		pd.setCancelable(cancleAble);
		pd.setOnCancelListener(new DialogInterface.OnCancelListener(){
			public void onCancel(DialogInterface dialog) {
				cancel(true);
			}
		});
		return setProgressDialog(pd);
	}
	public ResultAsyncTask<T> setOnSuccessRunnable(Runnable run){
		this.onSucRun=run;
		return this;
	}
	public ResultAsyncTask<T> setOnFailRunnable(Runnable run){
		this.onFailRun=run;
		return this;
	}
	/**�����Ƿ���ʾ�����ɹ�����ʧ�ܵ�֪ͨ��û������OnFailRun,OnSucRun��ʱ�� */
	public ResultAsyncTask<T> setToast(boolean isToast){
		this.isSucToast=isToast;
		this.isFailToast=isToast;
		return this;
	}
	/**�����Ƿ���ʾ�����ɹ�����ʧ�ܵ�֪ͨ��û������OnFailRun,OnSucRun��ʱ�� */
	public ResultAsyncTask<T> setToast(boolean isSucToast,boolean isFailToast){
		this.isSucToast=isSucToast;
		this.isFailToast=isFailToast;
		return this;
	}
	/**�����Ƿ���ʾ�����ɹ�����ʧ�ܵ�֪ͨ��û������OnFailRun,OnSucRun��ʱ�� */
	public ResultAsyncTask<T> setToast(String toast){
		return setToast(toast, toast);
	}
	/**�����Ƿ���ʾ�����ɹ�����ʧ�ܵ�֪ͨ��û������OnFailRun,OnSucRun��ʱ�� */
	public ResultAsyncTask<T> setToast(String sucToast,String failToast){
		this.sucToast=sucToast;
		if(sucToast!=null) isSucToast=true;
		this.failToast=failToast;
		if(failToast!=null) isFailToast=true;
		return this;
	}
	private String getSucToast(){
		if(sucToast==null) return "�������";
		else return sucToast;
	}
	private String getFailToast(){
		if(failToast==null) return "����ʧ��";
		else return failToast;
	}
	/**��������Ƿ���ʧprogressDialog��Ĭ��Ϊtrue */
	public ResultAsyncTask<T> setDismissPd(boolean isDismissPd){
		this.isDismissPd=isDismissPd;
		return this;
	}
	@Override
	protected void onPostExecute(T result) {
		if(pd!=null && isDismissPd) pd.dismiss();
		if(result==null|| (result instanceof Boolean && !(Boolean)result) ){
			if(onFailRun==null&&isFailToast) Toast.makeText(context, getFailToast(), Toast.LENGTH_SHORT).show();
			if(onFailRun!=null) onFailRun.run();
		}else{
			if(onSucRun==null&&isSucToast) Toast.makeText(context, getSucToast(), Toast.LENGTH_SHORT).show();
			if(onSucRun!=null) onSucRun.run();
		}
	}
	
	@SuppressLint("NewApi")
	public void execute(){
		if(pd!=null&&!pd.isShowing()) pd.show();
		if(android.os.Build.VERSION.SDK_INT<11) super.execute();
		else executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
}

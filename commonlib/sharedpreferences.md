# SharedPreferencesManger
## Before use it
- 1. init SharedPreferencesManger
```
 public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferencesDao.initSharePrefenceDao(getApplicationContext());  //init SharedPreferencesManger
	}
}
```

- 2.how to save data
saveData(String key, Object value)

- 3.how to get data
<T> T getData(String key, Object defValue, Class&lt;T> clazz);

## Demo set and get data
```
   String jsonStr="1234567890-==-098765431";
   SharedPreferencesDao.getInstance().saveData("jsonStr",jsonStr);

   SharedPreferencesDao.getInstance().saveData("test1",1111);
   int test1=SharedPreferencesDao.getInstance().getData("test1",-1,Integer.class); //not good enough
   int testTemp=SharedPreferencesDao.getInstance().getData("testTemp",250,Integer.class);  //have nokey testtemp
```
## Multi Process

   If Multi Process,use 		sharedPreferencesDao = new SharedPreferencesDao(Context.MODE_MULTI_PROCESS);
   More ,see     public abstract class Context (filed MODE_MULTI_PROCESS)
   public static final int MODE_MULTI_PROCESS = 0x0004

   > you must know https://developer.android.com/reference/android/content/Context.html#MODE_MULTI_PROCESS
   > so,use it https://github.com/grandcentrix/tray

## Advice?

Please contact me at anylife.zlb@gmail.com ,thank you .


反射练习：http://blog.csdn.net/nugongahou110/article/details/46913461

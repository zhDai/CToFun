package DaraDeal;

public class SQLHelperString {
	private StringBuilder sql = new StringBuilder();
	
	public SQLHelperString(String name){
		sql.append("select " + name + "(");
	}

	public  SQLHelperString put(String value){
		if(value != null)
		{
			sql.append("'" + value + "',");
		}else{
			sql.append("null,");
		}
		return this;
	}

	public  SQLHelperString put( Double value){
		if(value != null)
		{
			sql.append( value.toString() + ",");
		}else{
			sql.append("null,");
		}
		return this;
	}

	public  SQLHelperString put( Integer value){
		if(value != null){
			sql.append( value.toString() + ",");
		}else{
			sql.append("null,");
		}
		return this;
	}
	
	String getSQL(){
		return sql.substring(0,sql.length()-1) + ");";
	}

}

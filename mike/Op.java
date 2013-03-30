package mike;

import java.util.*;

public abstract class Op {
	public final int args = 0;
	public abstract void apply( Machine machine, ArrayList< Value > args );
}

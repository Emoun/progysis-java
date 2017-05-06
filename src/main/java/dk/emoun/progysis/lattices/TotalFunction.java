package dk.emoun.progysis.lattices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Represents a Total Function of the key elements K, Complete Lattice L, and lattice elements V.<br>
 * Each instance of a {@link TotalFunction} can only map a specific set of keys. 
 * If two instances of TotalFunction of the same class map to different sets of keys, 
 * they will always be incomparable. 
 * Additionally, the methods {@link #getBottom()} and {@link #getTop()}
 * return instances that map the same keys (no more or less) 
 * than the instance that the method was invoked on. These top and bottom instance will
 * also be incomparable to any instance that maps a different key set.<br>
 * <br>
 * 
 * @param <R>
 * The type of the {@link TotalFunction Total Function} extender(IE R should be the exact same type as the extending class)
 * @param <K>
 * The key type of the Total Function. 
 * I.E. The K in the Total Function definition: (K->L, <=)
 * @param <L>
 * The type of the Complete Lattice the total function space maps to. 
 * I.E. The L in the Total Function definition: (K->L, <=)
 * @param <V>
 * The value type of the Complete Lattice L's elements.
 */
public abstract class TotalFunction
	<
		R extends TotalFunction<R,K,L,V>,
		K,
		L extends CompleteLattice<V>,
		V extends LatticeElement<V>
	> 
	implements CompleteLattice<R>, LatticeElement<R> 
{
	
//Fields	
	/**
	 * Mapping of keys to lattice elements.<br>
	 * Effectively the state of the Total Function.<br>
	 * Given a Total Function 'f', 'f(s)' == mapping.get(s).
	 */
	private Map<K,V> mapping;
	
	/**
	 * The instance of a Complete Lattice the Total Function maps to.
	 */
	private L lattice;
//Constructors
	/**
	 * Constructs a Total Function over the given lattice, mapping the given keys.
	 * @param lattice
	 * The Complete Lattice the resulting TotalFunction maps to.
	 * @param keys
	 * Specific key values the TotalFunction is to map. <br>
	 * Duplicate keys are only mapped once by the resulting TotalFunction.<br>
	 * All keys evaluate to 'lattice.getBottom()' when the constructor terminates.<br>
	 * 
	 */
	public TotalFunction(L lattice, K... keys){
		this(new HashMap<K,V>(), lattice);
		for(K key: keys){
			this.mapping.put(key, lattice.getBottom());
		}
	}
	
	/**
	 * Constructs a Total Function over the given lattice, with the given mapping.
	 * @param mapping
	 * Mapping from a key set to elements of the given lattice.
	 * @param lattice
	 * The Complete Lattice the resulting TotalFunction maps to.
	 */
	protected TotalFunction(Map<K,V> mapping, L lattice){
		this.mapping = mapping;
		this.lattice = lattice;
	}
	
//Abstract methods
	/**
	 * Constructs a new Total Function with the given mapping to the same lattice as
	 * the instance this method is invoked on.<br>
	 * Use the super(mapping) constructor to achieve this.<br>
	 * <br>
	 * Simply, an implementation could be:<br>
	 * protected R constructTotalFunction(Map<K,V> mapping){<br>
	 * return super(mapping);<br>
	 * }<br>
	 * 
	 * @param mapping
	 * The mapping of a key set to elements of the same lattice 
	 * as the instance this method is invoked on.
	 * @return
	 * The newly constructed instance.
	 */
	protected abstract R constructTotalFunction(Map<K,V> mapping);
	
	
//Methods

	/**
	 * Evaluates the Total Function mapping of the given key.<br>
	 * @param key
	 * The key mapping to evaluate.
	 * @return
	 * The element the given key maps to in the TotalFunction this method is invoked on.<br>
	 * If the TotalFunction does not map that key, {@code null} is returned.
	 * @throws UnmappedKeyException
	 * if the Total Function instance does not map the given key.
	 */
	public V getValue(K key) throws UnmappedKeyException{
		V value = mapping.get(key);
		if(value ==  null){ 
			throw new UnmappedKeyException(key);	
		}
		return value; 
	}
	
	/**
	 * Returns a new Total Function instance where the given key mapping holds.<br>
	 * If the instance does not already map the given key,
	 * the resulting instance will map the given key in addition to
	 * all the keys the invoking instance maps.
	 * @param key
	 * The key value to map
	 * @param e
	 * Element of the lattice both the invoking and resulting instances map to.
	 * @return
	 * a new Total Function that is identical to the invoking instance except that it (additionally?)
	 * maps the given key to the given lattice element.<br>
	 */
	public R getUpdateValue(K key, Evaluable<V> e){
		HashMap<K,V> newMapping = new HashMap<K,V>();
		newMapping.putAll(mapping);
		newMapping.put(key, e.value());
		return constructTotalFunction(newMapping);
	}
	
	/**
	 * Constructs a new Total Function instance that maps the given key to
	 * the same lattice as the invoking instance.<br>
	 * All the keys in the resulting instance map to the bottom value of the lattice.
	 * @param keys
	 * Key set the resulting instance maps. Duplicates are only mapped once.
	 * @return
	 * A new Total function that maps the given keys to bottom
	 */
	public R constructBottomTotalFunction(K... keys){
		Map<K, V> mapping = new HashMap<K, V>();
		for(K key: keys){
			mapping.put(key, lattice.getBottom());
		}
		return constructTotalFunction(mapping);
	}
	
//Accessors
	
	/**
	 * @return
	 * The instance of a Complete Lattice the Total Function instance maps to
	 */
	public L getLattice(){
		return this.lattice;
	}
	
//Overriding methods
		
	@Override
	public R getBottom() {
		return copyThisToFunctionWhereAllKeysMapTo(lattice.getBottom());
	}
	
	@Override
	public boolean isBottom(Evaluable<R> r) {
		return allValuesMapTo(r, lattice.getBottom());
	}

	@Override
	public boolean compare(Evaluable<R> e1, Evaluable<R> e2) throws IncomparableLatticeException {
		
		TotalFunction<R,K,L,V> e1TF = (TotalFunction) (e1.value());
		TotalFunction<R,K,L,V> e2TF = (TotalFunction) (e2.value());
		
		validateMapSameKeys(e1TF, e2TF);
		
		if(isBottom(e1))
		{
			return true;
		}
		
		if(isBottom(e2))
		{
			//e1 is not bot
			return false;
		}
		
		//At this point neither is bottom and they map the same keys
		for(K key: e1TF.mapping.keySet()){
			if(!lattice.compare(
					e1TF.getValue(key), 
					e2TF.getValue(key)
					))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public R join(Evaluable<R> e1, Evaluable<R> e2) {
		TotalFunction<R,K,L,V> e1TF = (TotalFunction) (e1.value());
		TotalFunction<R,K,L,V> e2TF = (TotalFunction) (e2.value());
		
		HashMap<K,V> newMapping = new HashMap<K,V>();
		
		//Get all keys from both functions
		Set<K> allKeys = joinKeySets(e1TF, e2TF);
		
		//Join the two values for each key and put it in the new map
		V v1, v2;
		for(K key: allKeys){
			v1 = getValueOrBottom(e1TF, key);
			v2 = getValueOrBottom(e2TF, key);
			
			newMapping.put(key, lattice.join(v1, v2));
		}
		
		return constructTotalFunction(newMapping);
	}
	
	@Override
	public R value(){
		return (R)this;
	};

	@Override
	public String stringRepresentation(Evaluable<R> r){
		TotalFunction<R, K, L, V> tF = (TotalFunction<R,K,L,V>)(r.value());
		StringBuilder b = new StringBuilder();
		
		b.append('{');
		for(Entry<K,V> e: tF.mapping.entrySet()){
			b.append('{');
			b.append(e.getKey().toString());
			b.append('=');
			b.append(tF.lattice.stringRepresentation(e.getValue()));
			b.append('}');
		}
		b.append('}');
		return b.toString();		
	}
		
//private methods
	
	/**
	 * Get the lattice element that the given Total Function maps the given to.<br>
	 * If the given function does not map the key, the bottom element of the lattice
	 * is returned instead.
	 * @param e
	 * @param key
	 * @return
	 */
	private V getValueOrBottom(TotalFunction<R, K, L, V> e, K key) {
		try{
			return e.getValue(key);
		}catch(UnmappedKeyException err){
			return lattice.getBottom();
		}
	}
	
	/**
	 * Returns a new set of keys, which contains all the keys both given Total Functions
	 * map.
	 * @param e1T
	 * @param e2T
	 * @return
	 */
	private Set<K> joinKeySets(TotalFunction<R, K, L, V> e1T, TotalFunction<R, K, L, V> e2T) {
		Set<K> allKeys = new HashSet<K>();
		allKeys.addAll(e1T.mapping.keySet());
		allKeys.addAll(e2T.mapping.keySet());
		return allKeys;
	}
	
	/**
	 * Validates that the given Total Functions map the exact same keys.<br>
	 * If this is not the case an {@link IncomparableLatticeException} is thrown.
	 * @param e1T
	 * @param e2T
	 * @throws IncomparableLatticeException
	 */
	private void validateMapSameKeys(TotalFunction<R, K, L, V> e1T, TotalFunction<R, K, L, V> e2T)
			throws IncomparableLatticeException 
	{
		if( !mapsKeysOf(e1T, e2T) || 
			!mapsKeysOf(e2T, e1T))
		{
			throw new IncomparableLatticeException((R) e1T, (R) e2T, this);
		}
	}
	
	/**
	 * Returns whether the first Total Function maps
	 * all of the second Total Function's keys.
	 * @param maps
	 * @param keys
	 * @return
	 * whether {@code maps} maps all of the keys {@code keys'} maps. 
	 */
	private boolean mapsKeysOf(TotalFunction<R, K, L, V> maps, TotalFunction<R, K, L, V> keys){
		for(K key: keys.mapping.keySet()){
			if(!maps.mapping.containsKey(key)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Copies the given maps' keys into a new map, and maps
	 * then all to the given value.
	 * @param value
	 * @return
	 * The resulting map
	 */
	private Map<K,V> copyKeysWithValue(Map<K,V> keys, V value) {
		Map<K,V> newKeyMapping = new HashMap<K,V>();
		for(K key: keys.keySet()){
			newKeyMapping.put(key, value);
		}
		
		return newKeyMapping;
	}
	
	/**
	 * Creates a new Total Function that maps the same keys
	 * as the invoking instance, but where all keys map to
	 * the given value.
	 * @param e
	 * Complete Lattice element to map all the keys to.
	 * @return
	 * The resulting Total Function instance
	 */
	private R copyThisToFunctionWhereAllKeysMapTo(V e) {
		return constructTotalFunction(copyKeysWithValue(mapping, e));
	}
	
	/**
	 * Returns whether the given Total Function maps
	 * all of its keys to the given value.
	 * @param r
	 * @param e
	 * @return
	 */
	private boolean allValuesMapTo(Evaluable<R> r, V e){
		TotalFunction<R,K,L,V> keyMappings = (TotalFunction)(r.value());
		for(Entry<K,V> mapping: keyMappings.mapping.entrySet()){
			if(!LatticeUtilities.equal(mapping.getValue(), e, lattice)){
				return false;
			}
		}
		return true;
	}
}

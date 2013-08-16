package ir.utopia.core.utilities.serialization;

import ir.utopia.core.persistent.UtopiaBasicPersistent;


public interface UtopiaSerializer {

	public String serializedToJSON(UtopiaBasicPersistent persistent);
}

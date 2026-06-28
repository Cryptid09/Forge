package com.forge.devopstoolbox.core.port;

import com.forge.devopstoolbox.core.model.AppMetadata;
import java.util.List;

public interface SchemaMetadataRepository {

    List<AppMetadata> findAll();
}

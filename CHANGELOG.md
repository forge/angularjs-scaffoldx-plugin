
# 1.0.4

## Bug Fixes
- FORGEPLUGINS-139. Ensure that templates installed in the project under 'src/main/templates' are used during scaffold generation.

## Features
- FORGEPLUGINS-159. Support Temporal fields of type date and time in the AngularJS scaffolding.

## Component upgrades
- FORGEPLUGINS-137. Upgrade to AngularJS 1.2.x. AngularJS 1.2.8 is now used.
- FORGEPLUGINS-155. Upgrade to Bootstrap 3. Bootstrap 3.0.3 is now used.
- FORGEPLUGINS-156. Upgrade to Forge API 1.4.3. The plugin now requires Forge 1.4.3.Final or higher.

# 1.0.3

## Bug Fixes
- FORGEPLUGINS-145. Wait until the Forge project Configuration is updated with any changes before completing the AngularJS scaffold setup.

# 1.0.2

## Bug Fixes

- FORGEPLUGINS-122. Display primary keys if they're not auto-generated Ids.
- FORGEPLUGINS-124. Allow installation of the scaffold via Facets API.
- FORGEPLUGINS-128. Ensure that AngularJS scaffold facet is not deactivated on exit from the project context.

## Features

- FORGEPLUGINS-125. Support Temporal fields of type datetime in the AngularJS scaffolding.
- FORGEPLUGINS-131. Support discovery of existing REST resources instead of assuming REST resources URL conventions.
- FORGEPLUGINS-126. The AngularJS scaffolding now supports the new REST resource representations for nested properties. Forge 1.4.0.Final supports the "Root and Nested DTO" paradigm for exposing REST resources instead of exposing JPA entities directly. With this change, the AngularJS scaffolding plugin can now ensure additions/deletions to OneToMany/ManyToMany attributes of entities are performed correctly on the server-side.
- FORGEPLUGINS-123. AngularJS scaffolding should scaffold @Embedded properties.

## Component upgrades

- Upgrade AngularJS to 1.0.7.
- Upgrade Forge API to 1.4.0.Final. The plugin now requires Forge 1.4.0.Final or higher.

# 1.0.1

## Bug Fixes

- Index file generation. The list of JPA entities appearing in the index file are generated based on the directories present in the '/views/' directory.

# 1.0.0

The initial release. Supports scaffolding of AngularJS web-apps from JPA entities. REST resources required at the server can be written manually or (preferably) generated using the Forge REST command.


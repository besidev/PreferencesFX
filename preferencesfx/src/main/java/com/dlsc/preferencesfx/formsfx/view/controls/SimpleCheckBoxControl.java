package com.dlsc.preferencesfx.formsfx.view.controls;

/* -
 * ========================LICENSE_START=================================
 * FormsFX
 * %%
 * Copyright (C) 2017 DLSC Software & Consulting
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import com.dlsc.formsfx.model.structure.MultiSelectionField;
import com.dlsc.preferencesfx.util.VisibilityProperty;

/**
 * This class provides the base implementation for a simple control to edit
 * checkbox values.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 * @author François Martin
 * @author Marco Sanfratello
 */
public class SimpleCheckBoxControl<V> extends SimpleControl<MultiSelectionField<V>, VBox> {

  /**
   * - The checkboxes list contains all the checkboxes to display.
   * - The node is a VBox holding all node.
   */
  protected final List<CheckBox> checkboxes = new ArrayList<>();

  /**
   * Constructs a SimpleCheckBoxControl of {@link SimpleCheckBoxControl} type, with visibility condition.
   *
   * @param visibilityProperty - property for control visibility of this element
   *
   * @return the constructed SimpleCheckBoxControl
   */
  public static <V> SimpleCheckBoxControl of(VisibilityProperty visibilityProperty) {
    SimpleCheckBoxControl<V> simpleCheckBoxControl = new SimpleCheckBoxControl<>();

    simpleCheckBoxControl.visibilityProperty = visibilityProperty;

    return simpleCheckBoxControl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    node = new VBox();
    node.getStyleClass().add("simple-checkbox-control");

    createCheckboxes();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    node.setSpacing(5);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    super.setupBindings();
    setupCheckboxBindings();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    super.setupValueChangedListeners();

    field.itemsProperty().addListener((observable, oldValue, newValue) -> {
      createCheckboxes();
      setupCheckboxBindings();
      setupCheckboxEventHandlers();
    });

    field.selectionProperty().addListener((observable, oldValue, newValue) -> {
      for (int i = 0; i < checkboxes.size(); i++) {
        checkboxes.get(i).setSelected(field.getSelection().contains(field.getItems().get(i)));
      }
    });

    field.errorMessagesProperty().addListener((observable, oldValue, newValue) ->
        toggleTooltip(node, checkboxes.get(checkboxes.size() - 1))
    );
    field.tooltipProperty().addListener((observable, oldValue, newValue) ->
        toggleTooltip(node, checkboxes.get(checkboxes.size() - 1))
    );

    for (int i = 0; i < checkboxes.size(); i++) {
      checkboxes.get(i).focusedProperty().addListener((observable, oldValue, newValue) ->
          toggleTooltip(node, checkboxes.get(checkboxes.size() - 1))
      );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    node.setOnMouseEntered(event -> toggleTooltip(node, checkboxes.get(checkboxes.size() - 1)));
    node.setOnMouseExited(event -> toggleTooltip(node, checkboxes.get(checkboxes.size() - 1)));
    setupCheckboxEventHandlers();
  }

  /**
   * This method creates node and adds them to checkboxes and is
   * used when the itemsProperty on the field changes.
   */
  protected void createCheckboxes() {
    node.getChildren().clear();
    checkboxes.clear();

    for (int i = 0; i < field.getItems().size(); i++) {
      CheckBox cb = new CheckBox();

      cb.setText(field.getItems().get(i).toString());
      cb.setSelected(field.getSelection().contains(field.getItems().get(i)));

      checkboxes.add(cb);
    }

    node.getChildren().addAll(checkboxes);
  }

  /**
   * Sets up bindings for all checkboxes.
   */
  protected void setupCheckboxBindings() {
    for (CheckBox checkbox : checkboxes) {
      checkbox.disableProperty().bind(field.editableProperty().not());
    }
  }

  /**
   * Sets up event handlers for all checkboxes.
   */
  protected void setupCheckboxEventHandlers() {
    for (int i = 0; i < checkboxes.size(); i++) {
      final int j = i;

      checkboxes.get(i).setOnAction(event -> {
        if (checkboxes.get(j).isSelected()) {
          field.select(j);
        } else {
          field.deselect(j);
        }
      });
    }
  }

}

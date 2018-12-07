package org.eclipse.xtext.idea.facet;

import com.intellij.facet.ui.FacetEditorValidator;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.facet.ui.ValidationResult;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import java.awt.GridBagConstraints;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.eclipse.xtext.idea.facet.GeneratorConfigurationState;
import org.eclipse.xtext.idea.util.IdeaWidgetFactory;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * Created by dhuebner on 19.06.15.
 */
@SuppressWarnings("all")
public class GeneratorFacetForm {
  @Extension
  private IdeaWidgetFactory _ideaWidgetFactory = new IdeaWidgetFactory();
  
  protected JCheckBox activated;
  
  protected TextFieldWithBrowseButton directory;
  
  protected TextFieldWithBrowseButton testDirectory;
  
  protected JCheckBox createDirectory;
  
  protected JCheckBox overwriteFiles;
  
  protected JCheckBox deleteGenerated;
  
  private com.intellij.openapi.module.Module module;
  
  private FacetValidatorsManager validatorsManager;
  
  private JComponent rootPanel;
  
  public GeneratorFacetForm(final com.intellij.openapi.module.Module module) {
    this(module, null);
  }
  
  public GeneratorFacetForm(final com.intellij.openapi.module.Module module, final FacetValidatorsManager validatorsManager) {
    this.module = module;
    this.validatorsManager = validatorsManager;
  }
  
  protected JComponent createComponent() {
    final Function1<IdeaWidgetFactory.TwoColumnPanel, IdeaWidgetFactory.TwoColumnPanel> _function = (IdeaWidgetFactory.TwoColumnPanel it) -> {
      IdeaWidgetFactory.TwoColumnPanel _xblockexpression = null;
      {
        final Function1<GridBagConstraints, JComponent> _function_1 = (GridBagConstraints it_1) -> {
          return this._ideaWidgetFactory.separator("General");
        };
        it.row(it, _function_1);
        this.createGeneralSection(it);
        final Function1<GridBagConstraints, JComponent> _function_2 = (GridBagConstraints it_1) -> {
          return this._ideaWidgetFactory.label(" ");
        };
        it.row(it, _function_2);
        final Function1<GridBagConstraints, JComponent> _function_3 = (GridBagConstraints it_1) -> {
          return this._ideaWidgetFactory.separator("Output Folder");
        };
        it.row(it, _function_3);
        this.createOutputSection(it);
        final Function1<GridBagConstraints, JComponent> _function_4 = (GridBagConstraints it_1) -> {
          JLabel _xblockexpression_1 = null;
          {
            this._ideaWidgetFactory.expand(it_1, GridBagConstraints.VERTICAL);
            _xblockexpression_1 = this._ideaWidgetFactory.label("");
          }
          return _xblockexpression_1;
        };
        _xblockexpression = it.row(it, _function_4);
      }
      return _xblockexpression;
    };
    return this._ideaWidgetFactory.twoColumnPanel(_function);
  }
  
  public void createOutputSection(@Extension final IdeaWidgetFactory.TwoColumnPanel it) {
    final Function1<GridBagConstraints, JComponent> _function = (GridBagConstraints it_1) -> {
      return this._ideaWidgetFactory.label("Directory:");
    };
    final Function1<GridBagConstraints, JComponent> _function_1 = (GridBagConstraints it_1) -> {
      TextFieldWithBrowseButton _xblockexpression = null;
      {
        it_1.weightx = 1.0;
        _xblockexpression = this.directory = this._ideaWidgetFactory.browseField(this.module.getProject());
      }
      return _xblockexpression;
    };
    it.row(it, _function, _function_1);
    final Function1<GridBagConstraints, JComponent> _function_2 = (GridBagConstraints it_1) -> {
      return this._ideaWidgetFactory.label("Test Directory:");
    };
    final Function1<GridBagConstraints, JComponent> _function_3 = (GridBagConstraints it_1) -> {
      return this.testDirectory = this._ideaWidgetFactory.browseField(this.module.getProject());
    };
    it.row(it, _function_2, _function_3);
    final Function1<GridBagConstraints, JComponent> _function_4 = (GridBagConstraints it_1) -> {
      return this.createDirectory = this._ideaWidgetFactory.checkBox("Create directory if it doesn\'t exist");
    };
    it.row(it, _function_4);
    final Function1<GridBagConstraints, JComponent> _function_5 = (GridBagConstraints it_1) -> {
      return this.overwriteFiles = this._ideaWidgetFactory.checkBox("Overwrite existing files");
    };
    it.row(it, _function_5);
    final Function1<GridBagConstraints, JComponent> _function_6 = (GridBagConstraints it_1) -> {
      return this.deleteGenerated = this._ideaWidgetFactory.checkBox("Delete generated files");
    };
    it.row(it, _function_6);
    this.registerDirectoryValidator(this.directory, "The output directory should belong to the module.");
    this.registerDirectoryValidator(this.testDirectory, "The output test directory should belong to the module.");
  }
  
  protected void registerDirectoryValidator(final TextFieldWithBrowseButton directory, final String errorMessage) {
    if ((this.validatorsManager == null)) {
      return;
    }
    final FacetEditorValidator _function = new FacetEditorValidator() {
      @Override
      public ValidationResult check() {
        boolean _isUnderModule = GeneratorFacetForm.this.isUnderModule(directory);
        boolean _not = (!_isUnderModule);
        if (_not) {
          return new ValidationResult(errorMessage);
        }
        return ValidationResult.OK;
      }
    };
    this.validatorsManager.registerValidator(_function, directory);
  }
  
  protected boolean isUnderModule(final TextFieldWithBrowseButton directory) {
    final String path = directory.getText();
    boolean _isAbsolute = FileUtil.isAbsolute(path);
    boolean _not = (!_isAbsolute);
    if (_not) {
      return true;
    }
    final VirtualFile root = IterableExtensions.<VirtualFile>head(((Iterable<VirtualFile>)Conversions.doWrapArray(ModuleRootManager.getInstance(this.module).getContentRoots())));
    return ((root != null) && FileUtil.isAncestor(root.getPath(), path, false));
  }
  
  public IdeaWidgetFactory.TwoColumnPanel createGeneralSection(@Extension final IdeaWidgetFactory.TwoColumnPanel it) {
    final Function1<GridBagConstraints, JComponent> _function = (GridBagConstraints it_1) -> {
      return this.activated = this._ideaWidgetFactory.checkBox("Compiler is activated");
    };
    return it.row(it, _function);
  }
  
  public void setData(final GeneratorConfigurationState data) {
    this.createDirectory.setSelected(data.isCreateDirectory());
    this.overwriteFiles.setSelected(data.isOverwriteExisting());
    this.deleteGenerated.setSelected(data.isDeleteGenerated());
    this.activated.setSelected(data.isActivated());
    this.directory.setText(data.getOutputDirectory());
    this.testDirectory.setText(data.getTestOutputDirectory());
  }
  
  public void getData(final GeneratorConfigurationState data) {
    data.setCreateDirectory(this.createDirectory.isSelected());
    data.setOverwriteExisting(this.overwriteFiles.isSelected());
    data.setDeleteGenerated(this.deleteGenerated.isSelected());
    data.setActivated(this.activated.isSelected());
    data.setOutputDirectory(this.directory.getText());
    data.setTestOutputDirectory(this.testDirectory.getText());
  }
  
  public boolean isModified(final GeneratorConfigurationState data) {
    boolean _isSelected = this.createDirectory.isSelected();
    boolean _isCreateDirectory = data.isCreateDirectory();
    boolean _tripleNotEquals = (Boolean.valueOf(_isSelected) != Boolean.valueOf(_isCreateDirectory));
    if (_tripleNotEquals) {
      return true;
    }
    boolean _isSelected_1 = this.overwriteFiles.isSelected();
    boolean _isOverwriteExisting = data.isOverwriteExisting();
    boolean _tripleNotEquals_1 = (Boolean.valueOf(_isSelected_1) != Boolean.valueOf(_isOverwriteExisting));
    if (_tripleNotEquals_1) {
      return true;
    }
    boolean _isSelected_2 = this.deleteGenerated.isSelected();
    boolean _isDeleteGenerated = data.isDeleteGenerated();
    boolean _tripleNotEquals_2 = (Boolean.valueOf(_isSelected_2) != Boolean.valueOf(_isDeleteGenerated));
    if (_tripleNotEquals_2) {
      return true;
    }
    boolean _isSelected_3 = this.activated.isSelected();
    boolean _isActivated = data.isActivated();
    boolean _tripleNotEquals_3 = (Boolean.valueOf(_isSelected_3) != Boolean.valueOf(_isActivated));
    if (_tripleNotEquals_3) {
      return true;
    }
    boolean _xifexpression = false;
    String _text = this.directory.getText();
    boolean _tripleNotEquals_4 = (_text != null);
    if (_tripleNotEquals_4) {
      boolean _equals = this.directory.getText().equals(data.getOutputDirectory());
      _xifexpression = (!_equals);
    } else {
      String _outputDirectory = data.getOutputDirectory();
      _xifexpression = (_outputDirectory != null);
    }
    if (_xifexpression) {
      return true;
    }
    boolean _xifexpression_1 = false;
    String _text_1 = this.testDirectory.getText();
    boolean _tripleNotEquals_5 = (_text_1 != null);
    if (_tripleNotEquals_5) {
      boolean _equals_1 = this.testDirectory.getText().equals(data.getTestOutputDirectory());
      _xifexpression_1 = (!_equals_1);
    } else {
      String _testOutputDirectory = data.getTestOutputDirectory();
      _xifexpression_1 = (_testOutputDirectory != null);
    }
    if (_xifexpression_1) {
      return true;
    }
    return false;
  }
  
  public JComponent getRootComponent() {
    if ((this.rootPanel == null)) {
      this.rootPanel = this.createComponent();
      this.postCreateComponent();
    }
    return this.rootPanel;
  }
  
  public void postCreateComponent() {
    this.deleteGenerated.setVisible(false);
  }
  
  public com.intellij.openapi.module.Module getModule() {
    return this.module;
  }
}

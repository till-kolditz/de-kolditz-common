/*******************************************************************************
 * Copyright (c) 2012 Till Kolditz.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     Till Kolditz
 *******************************************************************************/
package de.kolditz.common.ui;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import de.kolditz.common.i18n.I18N;

/**
 * 
 * 
 * @author Till Kolditz - Till.Kolditz@gmail.com
 */
public class UIUtils
{
    public static class ExtendedMessageDialog extends MessageDialog
    {
        protected Exception e;

        /**
         * @param parentShell
         * @param dialogTitle
         * @param dialogTitleImage
         * @param dialogMessage
         * @param dialogImageType
         * @param dialogButtonLabels
         * @param defaultIndex
         */
        public ExtendedMessageDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage,
                String dialogMessage, int dialogImageType, String[] dialogButtonLabels, int defaultIndex)
        {
            super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels,
                    defaultIndex);
        }

        public ExtendedMessageDialog(Shell parentShell, Exception e)
        {
            super(parentShell, e.getClass().getSimpleName(), parentShell.getDisplay().getSystemImage(SWT.ICON_ERROR), e
                    .getMessage(), ERROR, new String[] {IDialogConstants.OK_LABEL}, 0);
            this.e = e;
        }

        @Override
        protected boolean isResizable()
        {
            return true;
        }

        @Override
        protected Control createCustomArea(Composite parent)
        {
            ExpandableComposite comp = new ExpandableComposite(parent, SWT.NONE, ExpandableComposite.TWISTIE
                    | ExpandableComposite.CLIENT_INDENT | ExpandableComposite.NO_TITLE_FOCUS_BOX
                    | ExpandableComposite.SHORT_TITLE_BAR);
            comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            comp.setText(I18N.get().getString(I18N.UIUTILS_EMD_EXPCOMP_TITLE));
            Text text = new Text(comp, SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY | SWT.MULTI);
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            text.setText(sw.toString());
            comp.setClient(text);
            comp.addExpansionListener(new ExpansionAdapter()
            {
                @Override
                public void expansionStateChanged(ExpansionEvent e)
                {
                    ExtendedMessageDialog.this.getShell().pack();
                }
            });
            return comp;
        }
    }

    public static void openError(Exception e)
    {
        new ExtendedMessageDialog(Display.getCurrent().getActiveShell(), e).open();
    }
}

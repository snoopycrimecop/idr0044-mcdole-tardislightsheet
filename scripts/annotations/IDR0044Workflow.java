/*
 *------------------------------------------------------------------------------
 *  Copyright (C) 2018 University of Dundee. All rights reserved.
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */

package annotations;

import static annotations.CSVTools.*;

/**
 * Generates the filePaths.tsv and annotation.csv from the provided assays.txt file.
 * 
 * @author Dominik Lindner &nbsp;&nbsp;&nbsp;&nbsp; <a
 *         href="mailto:d.lindner@dundee.ac.uk">d.lindner@dundee.ac.uk</a>
 */
public class IDR0044Workflow {

    public static void main(String[] args) throws Exception {
        
        // ====================
        // Parameters
        final String basedir = "/Users/dlindner/Repositories";
        
        final String assayFile = basedir+"/idr0044-mcdole-tardislightsheet/experimentA/idr0044-experimentA-assays.txt";
        
        final String annotationFile = basedir+"/idr0044-mcdole-tardislightsheet/experimentA/idr0044-experimentA-annotation.csv";
        
        // =====================
        
        final char TSV = '\t';
        final char CSV = ',';
        
        String input = readFile(assayFile);

        /**
         * create annotation.csv
         */
            // Convert the tsv assays file to a csv file
            String annotationContent = format(input, TSV, CSV);
            
            // Remove empty columns
            annotationContent = removeEmptyColumns(annotationContent, CSV);
            
            int index = getColumnIndex(annotationContent, "Organism Term Source REF", CSV);
            annotationContent = removeColumn(annotationContent, index, CSV);
            annotationContent = addColumn(annotationContent, CSV, 3, "NCBITaxon", "Term Source 1 REF");
            annotationContent = addColumn(annotationContent, CSV, 4, "10090", "Term Source 1 Accession");
            
            index = getColumnIndex(annotationContent, "Image Name", CSV);
            annotationContent = process(annotationContent, index, CSV, s -> {
                // TODO: Need to change the image name the same way bioformats changes the name on import!
                return s;
            });
            
            // Finally save the annotion.csv file
            writeFile(annotationFile, annotationContent);
    }
}

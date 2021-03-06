/*
 * Copyright 2017 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apicurio.hub.api.storage.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * Can parse a DDL into a list of individual statements.
 * @author eric.wittmann@gmail.com
 */
public class DdlParser {

    /**
     * Constructor.
     */
    public DdlParser() {
    }

    /**
     * @param ddlFile
     */
    public List<String> parse(File ddlFile) {
        InputStream is = null;
        try {
            is = new FileInputStream(ddlFile);
            return parse(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * @param ddlStream
     * @throws IOException
     */
    public List<String> parse(InputStream ddlStream) throws IOException {
        List<String> rval = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(ddlStream, "UTF-8"));
        String line;
        StringBuilder builder = new StringBuilder();
        boolean isInMultiLineStatement = false;
        while ( (line = reader.readLine()) != null) {
            if (line.startsWith("--")) {
                continue;
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            if (line.endsWith("'") || line.endsWith("(")) {
                isInMultiLineStatement = true;
            }
            if (line.startsWith("'") || line.startsWith(")")) {
                isInMultiLineStatement = false;
            }
            builder.append(line);
            builder.append("\n");

            if (!isInMultiLineStatement) {
                String sqlStatement = builder.toString().trim();
                if (sqlStatement.endsWith(";")) {
                    sqlStatement = sqlStatement.substring(0, sqlStatement.length() - 1);
                }
                rval.add(sqlStatement);
                builder = new StringBuilder();
            }
        }
        return rval;
    }

}

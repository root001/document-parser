# **Phase 1: Foundation & Configuration**
**Goal:** Scaffold the project, parse configurations, and set up logging.

---

### **Prompt 1.1: Initialize Maven Project**
<code>  
**Task:**
- Add dependencies:  
  - SnakeYAML (`1.33`) for YAML parsing.  
  - Log4j 2 (`2.20.0`) for logging.  
- Add a `HealthCheck` CLI command (`--health`) returning `{"status": "ok"}`.  

**Test:**
- Run `mvn clean install` → Verify build success.
- Execute `java -jar target/parser.jar --health` → Validate output.
</code>

---

### **Prompt 1.2: YAML Configuration Loader**
<code>  
**Task:**  
- Create `Config.java` POJO with fields: `input.csv`, `input.xml`, `output.path`, etc.  
- Load `config.yaml` into `Config` using SnakeYAML.  
- Throw `ConfigException` if required fields are missing.  

**Test:**
- Unit test: Valid YAML → `Config` populated correctly.
- Unit test: Missing `input.csv` → Throws `ConfigException`.

**Integration:**
- Log loaded config at startup.  
  </code>

---

### **Prompt 1.3: Log4j 2 Setup**
<code>  
**Task:**  
- Add `log4j2.xml` with:  
  - File appender (logs to `parser.log`).  
  - Console appender with ANSI colors.  
- Use `LoggerFactory` to create loggers.  

**Test:**
- Log `INFO`/`ERROR` messages → Verify file/console output.
- Test log level changes via YAML (e.g., `DEBUG` → verbose logs).

**Integration:**
- Inject logger into `HealthCheck`.  
  </code>

---

# **Phase 2: Data Parsing**
**Goal:** Read and validate CSV/XML input.

---

### **Prompt 2.1: CSV Parser**
<code>  
**Task:**  
- Create `CsvParser.java` to parse CSV into `BrandRelease` records.  
- Reject rows with invalid dates (e.g., `13/20/2023`).  

**Test:**
- Unit test: Valid row → Correct `BrandRelease`.
- Unit test: Invalid date → `ParseException`.

**Integration:**
- Log parsed brands after CSV processing.  
  </code>

---

### **Prompt 2.2: XML Parser**
<code>  
**Task:**  
- Implement `XmlParser.java` using StAX (`javax.xml.stream`).  
- Extract `<model>`, `<type>`, and `<price>` elements.  

**Test:**
- Parse XML snippet → Validate `Car` POJO.
- Test 50MB+ XML file → No `OutOfMemoryError`.

**Integration:**
- Log parsed car count.  
  </code>

---

### **Prompt 2.3: Lookup File**
<code>  
**Task:**  
- Create `ModelLookup.java` to map `model`→`brand` from CSV.  
- Fallback to `brand="Unknown"` for missing models.  

**Test:**
- Unit test: Known model → Correct brand.
- Unit test: Unknown model → "Unknown".

**Integration:**
- Use lookup in `XmlParser`.  
  </code>

---

# **Phase 3: Data Merging**
**Goal:** Combine CSV/XML data.

---

### **Prompt 3.1: Merge Logic**
<code>  
**Task:**  
- Create `DataMerger.java` to apply CSV dates to XML models.  
- Convert dates to ISO-8601 using `LocalDate`.  

**Test:**
- Unit test: Toyota RAV4 → Inherits CSV date.
- Unit test: Missing brand → `releaseDate=null`.

**Integration:**
- Log merged cars.  
  </code>

---

# **Phase 4: Currency & Filtering**
**Goal:** Resolve prices and filter data.

---



---

### **Prompt 4.2: Filter Engine**
<code>  
**Task:**  
- Implement `BrandPriceFilter.java` using `Predicate<Car>`.  

**Test:**
- Unit test: Toyota @ $25k → Included.
- Unit test: Honda @ $18k → Excluded.

**Integration:**
- Apply filter before output.  
  </code>

---
---++++++++++++++
# **Phase 5: Output Generation**
**Goal:** Generate formatted outputs.

---

### **Prompt 5.1: Date Formatting Utility**
<code>  
**Task:**  
- Create a `DateFormatter` utility class to enforce the `yyyy,dd,MM` format for release dates.  
- Ensure all output formats (Table/JSON/XML) use this formatter.  
- Example: `01/15/2023` (CSV input) → `2023,15,01` (output).  

**Test:**
- Unit test: Verify `DateFormatter` converts `LocalDate` to `yyyy,dd,MM` string.
- Edge case: Single-digit days/months (e.g., `03` instead of `3`).  
  </code>

--- +++++++++

### **Prompt 5.2: Table Formatter**
<code>  
**Task:**  
- Update `TableFormatter` to include `Release Date (yyyy,dd,MM)` column.  
- Align columns dynamically (e.g., `Brand` 10 chars, `Release Date` 12 chars).  
- Example output:  
  ```plaintext  
  | Brand   | Model | Release Date (yyyy,dd,MM) |  
  |---------|-------|----------------------------|  
  | Toyota  | RAV4  | 2023,15,01                |  

---

### **Prompt 5.2: JSON/XML Output**
<code>  
**Task:**  
- Use Jackson to serialize `Car` list with `yyyy,dd,MM` dates.  

**Test:**
- Round-trip test: Serialize→deserialize→compare.
- Validate XML schema.

**Integration:**
- Write to `output/` path from config.  
  </code>

--- _+++++++++++++++++++++++--

# **Phase 6: Finalization**
**Goal:** Optimize and document.

---

### **Prompt 6.1: CLI Polish**
<code>  
**Task:**  
- Add `--help` command.  
- Show progress bar for large files.  

**Test:**
- Verify CLI args parsing.
- Benchmark progress updates.  
  </code>

---

### **Prompt 6.2: Documentation**
<code>  
**Task:**  
- Write README with usage examples.  
- Include sample config/input files.

**Integration:**
- Document date formats and error handling.  
  </code>
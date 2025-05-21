```markdown
# **Phase 1: Foundation & Configuration**  
**Goal:** Scaffold the project, parse configurations, and set up logging.  

---

### **Prompt 1.1: Initialize Maven Project**  
<code>  
**Task:**  
- Create a Maven project with `core`, `parsers`, and `utils` modules.  
- Add dependencies:  
  - SnakeYAML (`1.33`) for YAML parsing.  
  - Log4j 2 (`2.20.0`) for logging.  
- Add a `HealthCheck` CLI command (`--health`) returning `{"status": "ok"}`.  

**Test:**  
- Run `mvn clean install` → Verify build success.  
- Execute `java -jar core/target/core.jar --health` → Validate output.  

**Integration:**  
- Commit a minimal `pom.xml` with module/dependency definitions.  
</code>  

---

### **Prompt 1.2: YAML Configuration Loader**  
<code>  
**Task:**  
- Create `Config` POJO with fields: `input.csv`, `input.xml`, `output.path`, etc.  
- Load `config.yaml` into `Config` using SnakeYAML.  
- Throw `ConfigException` if required fields are missing.  

**Test:**  
- Unit test: Valid YAML → `Config` populated correctly.  
- Unit test: Missing `input.csv` → Throws `ConfigException`.  

**Integration:**  
- Wire `Config` into `HealthCheck` to log loaded config.  
</code>  

---

### **Prompt 1.3: Log4j 2 Setup**  
<code>  
**Task:**  
- Add `log4j2.xml` with:  
  - File appender (logs to `parser.log`).  
  - Console appender with ANSI colors.  
  - Dynamic log level (from `config.yaml`).  
- Create `LoggerContext` utility class.  

**Test:**  
- Log `INFO`/`ERROR` messages → Verify file/console output.  
- Test log level changes via YAML (e.g., `DEBUG` → verbose logs).  

**Integration:**  
- Inject `Logger` into `HealthCheck` to log startup status.  
</code>  

---

# **Phase 2: Data Parsing**  
**Goal:** Read and validate CSV/XML input.  

---

### **Prompt 2.1: CSV Parser**  
<code>  
**Task:**  
- Create `CsvParser` class in `parsers` module.  
- Parse CSV rows into `BrandRelease` records (`brand`, `releaseDate`).  
- Reject rows with invalid dates (e.g., `13/20/2023`).  

**Test:**  
- Unit test: Valid CSV row → Correct `BrandRelease`.  
- Unit test: Invalid date → `ParseException` thrown.  

**Integration:**  
- Wire `CsvParser` into `HealthCheck` to log parsed brands.  
</code>  

---

### **Prompt 2.2: XML Parser with StAX**  
<code>  
**Task:**  
- Implement `XmlParser` using StAX (`javax.xml.stream`).  
- Extract `<model>`, `<type>`, and `<price>` elements.  
- Store prices as `Map<String, BigDecimal>` (currency → amount).  

**Test:**  
- Parse XML snippet → Validate `Car` POJO with model/type/prices.  
- Test large XML file (50MB+) → No `OutOfMemoryError`.  

**Integration:**  
- Log parsed car count after XML processing.  
</code>  

---

### **Prompt 2.3: Lookup File Integration**  
<code>  
**Task:**  
- Create `ModelLookup` class to load `model_brand.csv`.  
- Map `model` → `brand` (fallback to `Unknown`).  

**Test:**  
- Unit test: Existing model → Correct brand.  
- Unit test: Missing model → `brand="Unknown"`.  

**Integration:**  
- Inject `ModelLookup` into `XmlParser` to resolve brands.  
</code>  

---

# **Phase 3: Data Merging**  
**Goal:** Combine CSV/XML data with release dates.  

---

### **Prompt 3.1: Merge Logic**  
<code>  
**Task:**  
- Create `DataMerger` to combine `BrandRelease` (CSV) and `Car` (XML).  
- For each `Car`, apply the CSV `releaseDate` of its brand.  

**Test:**  
- Unit test: Toyota RAV4 → Inherits CSV Toyota’s release date.  
- Unit test: Brand missing in CSV → `releaseDate=null`.  

**Integration:**  
- Log merged cars with brand/date/price.  
</code>  

---

### **Prompt 3.2: Date Standardization**  
<code>  
**Task:**  
- Convert `MM/dd/yyyy` dates to `LocalDate` (ISO-8601).  
- Add `DateUtils` with `parseAndValidate()` method.  

**Test:**  
- Unit test: `01/15/2023` → `2023-01-15`.  
- Unit test: `13/20/2023` → `ParseException`.  

**Integration:**  
- Use `DateUtils` in `CsvParser` and `DataMerger`.  
</code>  

---

# **Phase 4: Currency & Filtering**  
**Goal:** Resolve prices and filter data.  

---

### **Prompt 4.1: Currency Resolution**  
<code>  
**Task:**  
- Create `CurrencyResolver` to select price by:  
  1. `type→currency` (SUV→EUR).  
  2. `defaultCurrency` (from YAML).  
- Add `strictMode` to throw error if no price found.  

**Test:**  
- Unit test: SUV with EUR price → EUR selected.  
- Unit test: Missing currency + `strictMode=true` → `CurrencyException`.  

**Integration:**  
- Inject `CurrencyResolver` into `DataMerger`.  
</code>  

---

### **Prompt 4.2: Filter Engine**  
<code>  
**Task:**  
- Implement `BrandPriceFilter` for `brand=Toyota AND price>20000`.  
- Use Predicate<Car> to filter merged data.  

**Test:**  
- Unit test: Toyota RAV4 @ $25000 → Included.  
- Unit test: Honda Civic @ $18000 → Excluded.  

**Integration:**  
- Apply filter before passing data to output modules.  
</code>  

---

# **Phase 5: Output Generation**  
**Goal:** Generate Table/JSON/XML outputs.  

---

### **Prompt 5.1: Table Formatter**  
<code>  
**Task:**  
- Create `TableFormatter` with aligned columns.  
- Support UTF-8 characters (e.g., `Škoda`).  

**Test:**  
- Unit test: 5 cars → 5 rows in table.  
- Integration test: Verify console output matches expected format.  

**Integration:**  
- Wire `TableFormatter` to CLI `--format=table`.  
</code>  

---

### **Prompt 5.2: JSON/XML Serialization**  
<code>  
**Task:**  
- Use Jackson (`ObjectMapper`) to serialize `Car` list.  
- Add `@JsonFormat` to `releaseDate` (ISO-8601).  

**Test:**  
- Round-trip test: Serialize → Deserialize → Compare.  
- Validate XML against schema (e.g., `<cars>` root).  

**Integration:**  
- Write output to `output/` path from YAML.  
</code>  

---

# **Phase 6: Finalization**  
**Goal:** Optimize, test, and document.  

---

### **Prompt 6.1: CLI Polish**  
<code>  
**Task:**  
- Add `--help` with usage examples.  
- Show progress bar during large file processing.  

**Test:**  
- Execute `java -jar parser.jar --help` → Verify usage guide.  
- Benchmark progress updates (e.g., 10k records → 10% increments).  

**Integration:**  
- Update `HealthCheck` to include CLI args parsing.  
</code>  

---

### **Prompt 6.2: Stress Testing**  
<code>  
**Task:**  
- Generate 100k CSV/XML records for load testing.  
- Monitor memory/CPU during processing.  

**Test:**  
- Validate output count matches input (minus invalid records).  
- Ensure no `OutOfMemoryError` with 100k records.  

**Integration:**  
- Log performance metrics to `parser.log`.  
</code>  
```
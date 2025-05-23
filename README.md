# DocParser - Document Processing Tool

DocParser is a Spring Boot application that merges and processes car brand data from CSV and XML files into a unified output format (JSON or XML).

## Features
- Merge CSV and XML data sources
- Filter results by brand and price range
- Output in JSON or XML format
- Configurable logging levels
- Health check endpoint
- Progress tracking for large files
- Custom brand-model mappings

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.9+

1. Build the application:
```bash
mvn clean package
```

2. Get help:
```bash
java -jar target/docparser.jar --help
```

## Scenario Commands

### 1. Filtering Scenarios

# Filter by Brand and Price (Toyota between 20k-50k)
java -jar target/docparser.jar config-filter-brand-price.yaml

# Filter by Brand and Date (Honda between 2020-2025)
java -jar target/docparser.jar config-filter-brand-date.yaml

### 2. Sorting Scenarios

# Sort by Release Date (newest first)
java -jar target/docparser.jar config-sort-date-desc.yaml

# Sort by Price (highest first)
java -jar target/docparser.jar config-sort-price-desc.yaml

### 3. Output Format Scenarios

# Output as JSON
java -jar target/docparser.jar config-output-json.yaml

# Output as XML 
java -jar target/docparser.jar config-output-xml.yaml

# Output as Table
java -jar target/docparser.jar config-output-table.yaml

### 4. Currency Conversion Scenarios

# Convert currencies by vehicle type
java -jar target/docparser.jar config-currency-conversion.yaml

## Sample Config Files

### config-filter-brand-price.yaml
```yaml
inputCsv: data/CarsBrand.csv
inputXml: data/carsType.xml
outputPath: output/
outputFormat: json
filters:
  brandPrice:
    brand: "Toyota"
    minPrice: 20000
    maxPrice: 50000
```

### config-sort-price-desc.yaml  
```yaml
inputCsv: data/CarsBrand.csv
inputXml: data/carsType.xml 
outputPath: output/
outputFormat: json
sort:
  by: "price"
  order: "desc"
```

### config-output-table.yaml
```yaml
inputCsv: data/CarsBrand.csv
inputXml: data/carsType.xml
outputPath: output/
outputFormat: table
```

### config-currency-conversion.yaml
```yaml
inputCsv: data/CarsBrand.csv
inputXml: data/carsType.xml
outputPath: output/
currencyMapping:
  SUV: EUR
  Sedan: JPY
  Truck: USD
```

## Configuration

### config.yaml
```yaml
input_csv: brands.csv       # Path to CSV input file
input_xml: cars.xml         # Path to XML input file
output_path: output/        # Output directory
output_format: json         # Output format: json or xml
log_level: info             # Logging level: trace, debug, info, warn, error
```

## Sample Files

### Input: brands.csv
```csv
Brand,Product,ReleaseDate,Version
Toyota,RAV4,01/15/2023,v1.0
Honda,CR-V,02/20/2023,v2.0
Ford,F-150,03/10/2023,v1.5
```

### Input: cars.xml
```xml
<cars>
  <car>
    <type>SUV</type>
    <model>RAV4</model>
    <price currency="USD">25000.00</price>
    <priceList>
      <price currency="EUR">22000.00</price>
    </priceList>
  </car>
</cars>
```

### Output (JSON):
```json
[
  {
    "brandType": "SUV",
    "productName": "Toyota",
    "model": "RAV4",
    "releaseDate": "2023,15,01",
    "price": {
      "currency": "USD",
      "amount": 25000.0
    },
    "priceList": [
      {
        "currency": "USD",
        "amount": 25000.0
      }
    ]
  }
]
```

## Data Processing

### Date Formats
- Input CSV dates: `MM/dd/yyyy` (e.g. 01/15/2023)
- Output dates: `yyyy,dd,MM` (e.g. 2023,15,01)

### Brand Mapping
Default mappings (configurable in application.properties):
```
Toyota=RAV4
Honda=CR-V
Ford=F-150
```

## Error Handling

Common errors and solutions:

| Error | Solution |
|-------|----------|
| Missing required field | Check input files for all required columns |
| Invalid date format | Ensure dates match MM/dd/yyyy format |
| File not found | Verify file paths in config.yaml |
| Invalid price format | Prices must be numeric with optional decimal |

## Advanced Usage

### Filtering by Price Range
```bash
java -jar target/docparser.jar config.yaml --min-price 20000 --max-price 30000
```

### Health Check
```bash
java -jar target/docparser.jar --health
```
Returns: `{"status": "ok"}`

## Troubleshooting

**Q: No output files are generated**
- Check the output_path in config.yaml exists
- Verify input files contain valid data

**Q: Getting parsing errors**
- Validate CSV/XML files against sample formats
- Check logs for specific error details

## License
MIT License

## Support
For issues or questions, please open an issue on our GitHub repository.

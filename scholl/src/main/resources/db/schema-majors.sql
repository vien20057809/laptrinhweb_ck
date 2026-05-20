-- Chạy script này trên database schoolmanager (SQL Server)
-- trước khi dùng trang quản lý ngành / chương trình đào tạo.

IF OBJECT_ID(N'dbo.departments', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.departments (
        id   UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_departments PRIMARY KEY DEFAULT NEWID(),
        code NVARCHAR(50)     NOT NULL,
        name NVARCHAR(255)    NOT NULL
    );
END
GO

IF OBJECT_ID(N'dbo.majors', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.majors (
        id             UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_majors PRIMARY KEY DEFAULT NEWID(),
        department_id  UNIQUEIDENTIFIER NOT NULL,
        code           NVARCHAR(100)    NOT NULL,
        name           NVARCHAR(255)    NOT NULL,
        description    NVARCHAR(MAX)    NULL,
        effective_date DATETIME2        NULL,
        expiry_date    DATETIME2        NULL,
        created_at     DATETIME2        NOT NULL CONSTRAINT DF_majors_created_at DEFAULT SYSUTCDATETIME(),
        updated_at     DATETIME2        NULL,
        created_by     NVARCHAR(255)    NULL,
        updated_by     NVARCHAR(255)    NULL,
        deleted_at     DATETIME2        NULL,
        deleted_by     NVARCHAR(255)    NULL,
        is_active      BIT              NOT NULL CONSTRAINT DF_majors_is_active DEFAULT (1),
        CONSTRAINT FK_majors_departments FOREIGN KEY (department_id) REFERENCES dbo.departments (id)
    );

    CREATE INDEX IX_majors_department_id ON dbo.majors (department_id);
    CREATE INDEX IX_majors_code ON dbo.majors (code);
    CREATE UNIQUE INDEX UX_majors_code_active ON dbo.majors (code) WHERE deleted_at IS NULL;
END
GO

-- Nếu bảng majors đã tồn tại từ trước, chạy thêm (bỏ qua nếu index đã có):
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'UX_majors_code_active' AND object_id = OBJECT_ID(N'dbo.majors'))
BEGIN
    CREATE UNIQUE INDEX UX_majors_code_active ON dbo.majors (code) WHERE deleted_at IS NULL;
END
GO

-- Dữ liệu mẫu (chạy một lần nếu bảng trống)
IF NOT EXISTS (SELECT 1 FROM dbo.departments)
BEGIN
    INSERT INTO dbo.departments (id, code, name) VALUES
        ('11111111-1111-1111-1111-111111111101', N'CNTT', N'Khoa Công nghệ thông tin'),
        ('11111111-1111-1111-1111-111111111102', N'KT',   N'Khoa Kinh tế');
END
GO
